package dev.xirreal;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class Installation extends Structure {
    public int sku;
    public short versionMajor;
    public short versionMinor;
    public short versionPatch;
    public String installationPath;

    public static String getSKU(Installation installation) {
        return switch (installation.sku) {
            case 0 -> "NGFX_NSIGHT_SKU_UNKNOWN";
            case 1 -> "NGFX_NSIGHT_SKU_PUBLIC";
            case 2 -> "NGFX_NSIGHT_SKU_PRO";
            case 3 -> "NGFX_NSIGHT_SKU_INTERNAL";
            default -> "Unknown";
        };
    }

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("sku", "versionMajor", "versionMinor", "versionPatch", "installationPath");
    }
}
