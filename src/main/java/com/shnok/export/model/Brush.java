package com.shnok.export.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Brush {
    private String name;
    private int csgOper;
    private int polyFlags;
    private Vector3 mainScale;
    private Vector3 postScale;
    private Vector3 position;
    private Vector3 prePivot;
    private int modelIndex;
    private Model model;
}
