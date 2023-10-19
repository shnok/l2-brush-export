package com.shnok.export.parser;

import acmi.l2.clientmod.io.ObjectInput;
import acmi.l2.clientmod.unreal.UnrealRuntimeContext;
import acmi.l2.clientmod.unreal.properties.PropertiesUtil;
import com.shnok.export.model.Vector3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DataParser {

    static float readFloat(ObjectInput<UnrealRuntimeContext> input) {
        // Float
        float f = (float) PropertiesUtil.read(input, PropertiesUtil.Type.FLOAT, false, null, null);
        System.out.println("Float " + f);
        return f;
    }

    static String readString( ObjectInput<UnrealRuntimeContext> input) {
        // String
        String s = (String) PropertiesUtil.read(input, PropertiesUtil.Type.STR, false, null, null);
        System.out.println("String " + s);
        return s;
    }

    static int readName( ObjectInput<UnrealRuntimeContext> input) {
        // String
        int s = (int) PropertiesUtil.read(input, PropertiesUtil.Type.NAME, false, null, null);
        System.out.println("Name " + s);
        return s;
    }

    static int readObject( ObjectInput<UnrealRuntimeContext> input) {
        // String
        int s = (int) PropertiesUtil.read(input, PropertiesUtil.Type.OBJECT, false, null, null);
        System.out.println("Name " + s);
        return s;
    }

    static int readInt( ObjectInput<UnrealRuntimeContext> input) {
        // Byte
        int i = (int) PropertiesUtil.read(input, PropertiesUtil.Type.INT, false, null, null);
        System.out.println("Int " + i);
        return i;
    }

    static int readByte( ObjectInput<UnrealRuntimeContext> input) {
        // Byte
        int b = (int) PropertiesUtil.read(input, PropertiesUtil.Type.BYTE, false, null, null);
        System.out.println("Byte " + b);
        return b;
    }

    static Vector3 readVector3(ObjectInput<UnrealRuntimeContext> input) {
        // Float
        float x = (float) PropertiesUtil.read(input, PropertiesUtil.Type.FLOAT, false, null, null);
        float y = (float) PropertiesUtil.read(input, PropertiesUtil.Type.FLOAT, false, null, null);
        float z = (float) PropertiesUtil.read(input, PropertiesUtil.Type.FLOAT, false, null, null);

        System.out.println("Vector3 (" + x + "," + y + "," + z + ")");
        return new Vector3(x, y, z);
    }

    static void checkPolyFlags(int value) {
        System.out.println("Polyflags:");

        if(value == 0) {
            System.out.println("Default");
            return;
        }

        Map<String, Integer> flags = new HashMap<>();
        flags.put("PF_Invisible", 1);
        flags.put("PF_Masked", 2);
        flags.put("PF_Translucent", 4);
        flags.put("PF_NotSolid", 8);
        flags.put("PF_Environment", 16);
        flags.put("PF_ForceViewZone", 16);
        flags.put("PF_Semisolid", 32);
        flags.put("PF_Modulated", 64);
        flags.put("PF_FakeBackdrop", 128);
        flags.put("PF_TwoSided", 256);
        flags.put("PF_AutoUPan", 512);
        flags.put("PF_AutoVPan", 1024);
        flags.put("PF_NoSmooth", 2048);
        flags.put("PF_BigWavy", 4096);
        flags.put("PF_SpecialPoly", 4096);
        flags.put("PF_SmallWavy", 8192);
        flags.put("PF_Flat", 16384);
        flags.put("PF_LowShadowDetail", 32768);
        flags.put("PF_NoMerge", 65536);
        flags.put("PF_CloudWavy", 131072);
        flags.put("PF_DirtyShadows", 262144);
        flags.put("PF_BrightCorners", 524288);
        flags.put("PF_SpecialLit", 1048576);
        flags.put("PF_Gouraud", 2097152);
        flags.put("PF_NoBoundRejection", 2097152);
        flags.put("PF_Unlit", 4194304);
        flags.put("PF_HighShadowDetail", 8388608);
        flags.put("PF_Memorized", 16777216);
        flags.put("PF_RenderHint", 16777216);
        flags.put("PF_Selected", 33554432);
        flags.put("PF_Portal", 67108864);
        flags.put("PF_Mirrored", 134217728);
        flags.put("PF_Highlighted", 268435456);
        flags.put("unused", 536870912);
        flags.put("PF_FlatShaded", 1073741824);
        flags.put("PF_EdProcessed", 1073741824);
        flags.put("PF_RenderFog", 1073741824);
            /*flags.put("PF_EdCut", 2147483648);
            flags.put("PF_Occlude", 2147483648);*/

        List<String> setFlags = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : flags.entrySet()) {
            if ((value & entry.getValue()) != 0) {
                setFlags.add(entry.getKey());
            }
        }

        for (String flag : setFlags) {
            System.out.println(flag);
        }
    }
}
