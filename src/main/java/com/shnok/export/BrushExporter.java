package com.shnok.export;

import acmi.l2.clientmod.io.UnrealPackage;
import acmi.l2.clientmod.unreal.UnrealSerializerFactory;
import acmi.l2.clientmod.unreal.core.Object;
import acmi.l2.clientmod.unreal.properties.L2Property;
import com.shnok.export.model.Brush;
import com.shnok.export.model.Model;
import com.shnok.export.model.Poly;
import com.shnok.export.parser.BrushParser;
import com.shnok.export.parser.ModelParser;
import com.shnok.export.parser.PolyParser;

import java.util.ArrayList;
import java.util.List;

public class BrushExporter {
    public static List<Brush> processBrushByName(UnrealPackage up, UnrealSerializerFactory serializerFactory, String brushName) {
        String[] brushNames;
        if(brushName.contains(",")) {
            brushNames = brushName.split(",");
        } else {
            brushNames = new String[] { brushName };
        }

        List<Brush> brushes = new ArrayList<>();
        List<UnrealPackage.ExportEntry> exportEntries = up.getExportTable();
        for (int i = 0; i < exportEntries.size(); i++) {
            for(int b = 0; b < brushNames.length; b++) {
                if (exportEntries.get(i).getObjectInnerFullName().equals(brushNames[b])) {
                    UnrealPackage.ExportEntry brushEntry = (UnrealPackage.ExportEntry) up.getAt(i + 1);
                    Brush brush = buildBrush(up, serializerFactory, brushEntry);
                    if(brush == null) {
                        continue;
                    }

                    brushes.add(brush);
                }
            }
        }

        return brushes;
    }

    public static List<Brush> processAllBrushes(UnrealPackage up, UnrealSerializerFactory serializerFactory) {
        List<Brush> brushes = new ArrayList<>();
        List<UnrealPackage.ExportEntry> exportEntries = up.getExportTable();
        for (int i = 0; i < exportEntries.size(); i++) {
            if (exportEntries.get(i).getObjectInnerFullName().contains("Brush")) {
                UnrealPackage.ExportEntry brushEntry = (UnrealPackage.ExportEntry) up.getAt(i + 1);
                Brush brush = buildBrush(up, serializerFactory, brushEntry);
                if(brush == null) {
                    continue;
                }

                brushes.add(brush);
            }
        }

        return brushes;
    }

    public static Brush buildBrush(UnrealPackage up, UnrealSerializerFactory serializerFactory, UnrealPackage.ExportEntry brushEntry) {
        Brush brush = BrushParser.parse(up, brushEntry, serializerFactory);
        if(brush.getModelIndex() == 0) {
            System.out.println("ERROR: Brush entry doesn't have a brush property.");
            return null;
        }

        UnrealPackage.ExportEntry modelEntry = (UnrealPackage.ExportEntry) up.objectReference(brush.getModelIndex());
        Model model = ModelParser.parse(modelEntry, serializerFactory);

        UnrealPackage.ExportEntry polyEntry = (UnrealPackage.ExportEntry) up.objectReference(model.getPolyIndex());
        Poly poly = PolyParser.parse(up, polyEntry, serializerFactory);

        model.setPoly(poly);
        brush.setModel(model);

        return brush;
    }
}
