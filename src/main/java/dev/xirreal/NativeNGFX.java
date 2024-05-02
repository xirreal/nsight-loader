package dev.xirreal;

import com.sun.jna.Library;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

public interface NativeNGFX extends Library {
    public int NGFX_Injection_EnumerateInstallations(IntByReference pCount, Installation[] pInstallations);
    public int NGFX_Injection_EnumerateActivities(Installation pInstallation, IntByReference pCount, Activity[] pActivities);
    public int NGFX_Injection_InjectToProcess(Installation pInstallation, Activity pActivity);
}
