package dev.xirreal;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Main implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("nsight-loader");
	@Override
	public void onInitialize() {
	    String os = System.getProperty("os.name").toLowerCase();
		if(!os.contains("win")) {
			LOGGER.error("Unsupported OS: " + os);
			return;
		}
		String arch = System.getProperty("os.arch").toLowerCase();
		if(!arch.contains("64")) {
			LOGGER.error("Unsupported architecture: " + arch);
			return;
		}

		String assetPath = "/assets/nsight-loader/NGFX_Injection.dll";
		String NGFX_DLL;

		try (InputStream in = Main.class.getResourceAsStream(assetPath)) {
			if (in == null) {
				throw new RuntimeException("DLL not found: " + assetPath);
			}
			File tempFile = File.createTempFile("NGFX_Injection", ".dll");
			tempFile.deleteOnExit();
			NGFX_DLL = tempFile.getAbsolutePath();

			try (FileOutputStream out = new FileOutputStream(tempFile)) {
				byte[] buffer = new byte[1024];
				int bytesRead;
				while ((bytesRead = in.read(buffer)) != -1) {
					out.write(buffer, 0, bytesRead);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to load DLL from " + assetPath, e);
		}

		try {
			NGFX ngfx = new NGFX(NGFX_DLL);
			LOGGER.info("NGFX loaded successfully");

			List<Installation> installations = ngfx.EnumerateInstallations();
			// Find newest installation
			Installation newestInstallation = installations.stream().max((a, b) -> {
				if(a.versionMajor != b.versionMajor) {
					return a.versionMajor - b.versionMajor;
				}
				if(a.versionMinor != b.versionMinor) {
					return a.versionMinor - b.versionMinor;
				}
				return a.versionPatch - b.versionPatch;
			}).orElse(null);
			if(newestInstallation == null) {
				LOGGER.error("No installations found");
				return;
			}
			LOGGER.info("Found installation: " + newestInstallation.installationPath);

			List<Activity> activities = ngfx.EnumerateActivities(newestInstallation);
			Activity activity = activities.stream().filter(a -> a.type == Activity.ActivityType.NGFX_INJECTION_ACTIVITY_FRAME_DEBUGGER).findFirst().orElse(null);

			if(activity == null) {
				LOGGER.error("Frame debugger is not available for this installation");
				return;
			}
			LOGGER.info("Found activity " + activity.getType() + ": "+ activity.description);
			Result result = ngfx.Inject(newestInstallation, activity);
			LOGGER.info("Injection result: " + result);
		} catch (Exception e) {
			LOGGER.error("Failed to load NGFX", e);
		}
	}
}