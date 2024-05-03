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

    public String getSku() {
        return switch (sku) {
            case SkuType.NGFX_NSIGHT_SKU_UNKNOWN -> "NGFX_NSIGHT_SKU_UNKNOWN";
            case SkuType.NGFX_NSIGHT_SKU_PUBLIC -> "NGFX_NSIGHT_SKU_PUBLIC";
            case SkuType.NGFX_NSIGHT_SKU_PRO -> "NGFX_NSIGHT_SKU_PRO";
            case SkuType.NGFX_NSIGHT_SKU_INTERNAL -> "NGFX_NSIGHT_SKU_INTERNAL";
            default -> "Unknown SKU";
        };
    }

    public interface SkuType {
        int NGFX_NSIGHT_SKU_UNKNOWN = 0;
        int NGFX_NSIGHT_SKU_PUBLIC = 1;
        int NGFX_NSIGHT_SKU_PRO = 2;
        int NGFX_NSIGHT_SKU_INTERNAL = 3;
    }

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("sku", "versionMajor", "versionMinor", "versionPatch", "installationPath");
    }
}
