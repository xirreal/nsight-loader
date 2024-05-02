package dev.xirreal;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class Activity extends Structure {
    public int type;
    public String description;


    public static interface ActivityType {
        int NGFX_INJECTION_ACTIVITY_UNKNOWN = 0;

        int NGFX_INJECTION_ACTIVITY_FRAME_DEBUGGER = 1;
        int NGFX_INJECTION_ACTIVITY_GENERATE_CPP_CAPTURE = 3;
        int NGFX_INJECTION_ACTIVITY_GPU_TRACE = 4;
        int NGFX_INJECTION_ACTIVITY_PYLON_CAPTURE = 5;

    }

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("type", "description");
    }
}