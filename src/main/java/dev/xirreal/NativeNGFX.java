package dev.xirreal;

import com.sun.jna.Library;
import com.sun.jna.ptr.IntByReference;

public interface NativeNGFX extends Library {
    int NGFX_Injection_EnumerateInstallations(IntByReference pCount, Installation[] pInstallations);
    int NGFX_Injection_EnumerateActivities(Installation pInstallation, IntByReference pCount, Activity[] pActivities);
    int NGFX_Injection_InjectToProcess(Installation pInstallation, Activity pActivity);
}
