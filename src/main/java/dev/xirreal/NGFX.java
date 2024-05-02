package dev.xirreal;

import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

import java.io.IOException;
import java.util.List;

public class NGFX implements NativeNGFX {
    private final NativeNGFX _native;

    public NGFX(final String dllPath) throws IOException {
        if(!new java.io.File(dllPath).exists()) {
            throw new IOException("DLL not found: " + dllPath);
        }
        
        _native = Native.load(dllPath, NativeNGFX.class);
    }

    public List<Installation> EnumerateInstallations() {
        IntByReference pCount = new IntByReference();
        int res = NGFX_Injection_EnumerateInstallations(pCount, null);
        if(res != 0) {
            throw new RuntimeException("Failed to enumerate installations: " + new Result(res));
        }

        int count = pCount.getValue();
        Installation[] installations = (Installation[]) (new Installation()).toArray(count);

        res = NGFX_Injection_EnumerateInstallations(pCount, installations);
        if(res != 0) {
            throw new RuntimeException("Failed to enumerate installations: " + new Result(res));
        }

        return List.of(installations);
    }

    public List<Activity> EnumerateActivities(Installation installation) {
        IntByReference pCount = new IntByReference();

        int res = NGFX_Injection_EnumerateActivities(installation, pCount, null);
        if(res != 0) {
            throw new RuntimeException("Failed to enumerate installations: " + new Result(res));
        }

        int count = pCount.getValue();
        Activity[] activities = (Activity[]) (new Activity()).toArray(count);

        res = NGFX_Injection_EnumerateActivities(installation, pCount, activities);
        if(res != 0) {
            throw new RuntimeException("Failed to enumerate activities: " + new Result(res));
        }

        return List.of(activities);
    }

    public Result Inject(Installation pInstallation, Activity pActivity) {
        return new Result(NGFX_Injection_InjectToProcess(pInstallation, pActivity));
    }

    @Override
    public int NGFX_Injection_EnumerateInstallations(IntByReference pCount, Installation[] pInstallations) {
        return _native.NGFX_Injection_EnumerateInstallations(pCount, pInstallations);
    }

    @Override
    public int NGFX_Injection_EnumerateActivities(Installation pInstallation, IntByReference pCount, Activity[] pActivities) {
        return _native.NGFX_Injection_EnumerateActivities(pInstallation, pCount, pActivities);
    }

    @Override
    public int NGFX_Injection_InjectToProcess(Installation pInstallation, Activity pActivity) {
        return _native.NGFX_Injection_InjectToProcess(pInstallation, pActivity);
    }
}
