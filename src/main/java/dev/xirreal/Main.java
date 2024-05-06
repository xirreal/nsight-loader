package dev.xirreal;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.swing.*;

public class Main implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("nsight-loader");

	public static String unpackResource(String resourceName) {
		String assetPath = "/assets/nsight-loader/" + resourceName;
		try (InputStream in = Main.class.getResourceAsStream(assetPath)) {
			if (in == null) {
				throw new RuntimeException("DLL not found: " + assetPath);
			}
			File tempFile = File.createTempFile("NGFX_Injection", ".dll");
			tempFile.deleteOnExit();

			try (FileOutputStream out = new FileOutputStream(tempFile)) {
				byte[] buffer = new byte[1024];
				int bytesRead;
				while ((bytesRead = in.read(buffer)) != -1) {
					out.write(buffer, 0, bytesRead);
				}
			}

			return tempFile.getAbsolutePath();
		} catch (IOException e) {
			throw new RuntimeException("Failed to load DLL from " + assetPath, e);
		}
	}

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

		int option = -1;
		String optionString = System.getProperty("debugger");
		if(optionString != null) {
			if(optionString.equalsIgnoreCase("renderdoc")) {
				option = JOptionPane.NO_OPTION;
			} else if(optionString.equalsIgnoreCase("nsight")) {
				option = JOptionPane.YES_OPTION;
			}
		}

		if(option == -1) {
			System.setProperty("java.awt.headless", "false");

			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ReflectiveOperationException | UnsupportedLookAndFeelException ignored) {
			}

			String[] options = {"NSight", "Renderdoc"};

			JFrame frame = new JFrame("Shader debugging");

			frame.setUndecorated( true );
			frame.setVisible( true );
			frame.setLocationRelativeTo( null );
			frame.requestFocus();

			option = JOptionPane.showOptionDialog(frame, "Pick the debugger to be loaded", "Shader debugging", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					options, null);

			frame.dispose();

			System.setProperty("java.awt.headless", "true"); // In case not having headless mode breaks something down the line...
		}

		if (option == JOptionPane.CLOSED_OPTION) {
			LOGGER.info("Skipping injection...");
			return;
		}

		if(option != JOptionPane.OK_OPTION) {
			LOGGER.info("Loading Renderdoc injector...");
			String RENDERDOC_DLL = unpackResource("renderdoc.dll");
			try {
				System.load(RENDERDOC_DLL);
				LOGGER.info("Renderdoc loaded successfully");
			} catch (UnsatisfiedLinkError e) {
				LOGGER.error("Failed to load Renderdoc: ", e);
			}
			return;
		}

		LOGGER.info("Loading NSight...");

		String NGFX_DLL = unpackResource("NGFX_Injection.dll");

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
			LOGGER.info("Found installation on " + newestInstallation.installationPath + ": drive");

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