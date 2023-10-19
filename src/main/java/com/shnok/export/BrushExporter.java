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

import java.util.List;
import java.util.Optional;

public class BrushExporter {

    UnrealSerializerFactory serializerFactory;

    public BrushExporter(UnrealSerializerFactory serializerFactory) {
        this.serializerFactory = serializerFactory;
    }

    public void processAllBrushes(UnrealPackage up) {
        List<UnrealPackage.ExportEntry> exportEntries = up.getExportTable();
        for (int i = 0; i < exportEntries.size(); i++) {
            if (exportEntries.get(i).getObjectInnerFullName().contains("Brush")) {
                UnrealPackage.ExportEntry brushEntry = (UnrealPackage.ExportEntry) up.getAt(i + 1);
                System.out.println("================> " + brushEntry.getObjectInnerFullName());
                Brush brush = BrushParser.parse(brushEntry, serializerFactory);
                if(brush.getModelIndex() == 0) {
                    System.out.println("ERROR: Brush entry doesn't have a brush property.");
                    continue;
                }

                UnrealPackage.ExportEntry modelEntry = (UnrealPackage.ExportEntry) up.objectReference(brush.getModelIndex());
                System.out.println("========> " + modelEntry.getObjectInnerFullName() + " found at index " + brush.getModelIndex());
                Model model = ModelParser.parse(modelEntry, serializerFactory);

                UnrealPackage.ExportEntry polyEntry = (UnrealPackage.ExportEntry) up.objectReference(model.getPolyIndex());
                System.out.println("====> " + polyEntry.getObjectInnerFullName() + " found at index " + model.getPolyIndex());
                Poly poly = PolyParser.parse(polyEntry, serializerFactory);

                model.setPoly(poly);
                brush.setModel(model);

                System.out.println("Brush done");
            }
        }
    }

}
