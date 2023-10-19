package com.shnok.export.parser;

import acmi.l2.clientmod.io.UnrealPackage;
import acmi.l2.clientmod.unreal.UnrealSerializerFactory;
import acmi.l2.clientmod.unreal.core.Object;
import acmi.l2.clientmod.unreal.properties.L2Property;
import com.shnok.export.model.Brush;
import com.shnok.export.model.Vector3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BrushParser extends DataParser {

    public static Brush parse(UnrealPackage up, UnrealPackage.ExportEntry entry, UnrealSerializerFactory serializerFactory) {
        Object brushObject = serializerFactory.getOrCreateObject(entry);
        List<L2Property> brushProperties = brushObject.properties;

        Brush brush = new Brush();
        brush.setName(entry.getObjectFullName());

        for(L2Property property : brushProperties) {
            java.lang.Object value = property.getAt(0);

            if(property.getName().contains("CsgOper")) {
                brush.setCsgOper(parseCsgOper((int) value));
            } else if(property.getName().contains("MainScale")) {
                List<L2Property> scaleStruct = (List<L2Property>) value;
                List<L2Property> scale = (List<L2Property>) scaleStruct.get(0).getAt(0);
                brush.setMainScale(new Vector3((float) scale.get(0).getAt(0), (float) scale.get(1).getAt(0), (float) scale.get(2).getAt(0)));
            } else if(property.getName().contains("PostScale")) {
                List<L2Property> scaleStruct = (List<L2Property>) value;
                List<L2Property> scale = (List<L2Property>) scaleStruct.get(0).getAt(0);
                brush.setPostScale(new Vector3((float) scale.get(0).getAt(0), (float) scale.get(1).getAt(0), (float) scale.get(2).getAt(0)));
            } else if(property.getName().contains("PolyFlags")) {
                brush.setPolyFlags(parsePolyFlags((int) value));
            } else if(property.getName().contains("Location")) {
                List<L2Property> pos = (List<L2Property>) value;
                brush.setPosition(new Vector3((float) pos.get(0).getAt(0), (float) pos.get(1).getAt(0), (float) pos.get(2).getAt(0)));
            } else if(property.getName().contains("PrePivot")) {
                List<L2Property> pivot = (List<L2Property>) value;
                brush.setPrePivot(new Vector3((float) pivot.get(0).getAt(0), (float) pivot.get(1).getAt(0), (float) pivot.get(2).getAt(0)));
            } else if(property.getName().contains("Group")) {
                brush.setGroup(up.nameReference((int) value));
            } else if(property.getName().equals("Brush")) {
                brush.setModelIndex((int) value);
            }
        }

        if(brush.getPolyFlags() == null) {
            brush.setPolyFlags(Arrays.asList("PF_Default"));
        }

        System.out.println(brush);

        return brush;
    }
}
