package com.shnok.export.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Brush {
    private String name;
    private String csgOper;
    private String group;
    private List<String> polyFlags;
    private Vector3 mainScale;
    private Vector3 postScale;
    private Vector3 position;
    private Vector3 prePivot;
    @JsonIgnore
    private int modelIndex;
    private Model model;
}
