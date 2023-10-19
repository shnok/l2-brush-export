package com.shnok.export;

import acmi.l2.clientmod.io.*;
import acmi.l2.clientmod.unreal.Environment;
import acmi.l2.clientmod.unreal.UnrealSerializerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shnok.export.model.Brush;
import com.shnok.export.model.DataContainer;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        String l2Folder = "D:\\Games\\Lineage II";
        String mapName = "17_25_Classic";
        String brushName = "";

        Environment environment = Environment.fromIni(new File(l2Folder + "\\system", "l2.ini"));
        UnrealSerializerFactory serializerFactory = new UnrealSerializerFactory(environment);

        UnrealPackage up = new UnrealPackage(new File(l2Folder + "\\maps", mapName + ".unr"), true);

        ObjectMapper objectMapper = new ObjectMapper();
        String json;

        List<Brush> brushes = new ArrayList<>();
        if(brushName.isEmpty()) {
            brushes = BrushExporter.processAllBrushes(up, serializerFactory);
        } else {
            Brush brush = BrushExporter.processBrushByName(up, serializerFactory, brushName);
            brushes.add(brush);
        }

        DataContainer dataContainer = new DataContainer(brushes);
        json = objectMapper.writeValueAsString(dataContainer);

        FileWriter fw = new FileWriter(mapName + ".json", false);
        BufferedWriter writer = new BufferedWriter(fw);
        writer.write(json);
        writer.close();

        /*UnrealPackage.ExportEntry entry = getEntry(up, "Polys933");
        PolyParser.parse(entry, serializerFactory);*/
    }

    static UnrealPackage.ExportEntry getEntry(UnrealPackage up, String entryName) {
        List<UnrealPackage.ExportEntry> exportEntries = up.getExportTable();
        for (int i = 0; i < exportEntries.size(); i++) {
            if (exportEntries.get(i).getObjectInnerFullName().equals(entryName)) {
                System.out.println("========================> " + exportEntries.get(i).getObjectFullName() + " - " + exportEntries.get(i).getObjectClass().getFullClassName());
                UnrealPackage.ExportEntry entry = (UnrealPackage.ExportEntry) up.getAt(i + 1);
                return entry;
            }
        }

        return null;
    }

    static UnrealPackage.ExportEntry GetEntryAtOffset(UnrealPackage up, int offset) {
        List<UnrealPackage.ExportEntry> exportEntries = up.getExportTable();
        for (int i = 0; i < exportEntries.size(); i++) {
            if (Math.abs(exportEntries.get(i).getOffset() - offset) <= 100) {
                System.out.println("========================> BINGO " + exportEntries.get(i).getObjectFullName() + " - " + exportEntries.get(i).getObjectClass().getFullClassName());
                UnrealPackage.ExportEntry entry = (UnrealPackage.ExportEntry) up.getAt(i + 1);
                return entry;
            }
        }

        return null;
    }
}



