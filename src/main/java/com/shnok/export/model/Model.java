package com.shnok.export.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Model {
    private String name;
    private Vector3 vec1;
    private Vector3 vec2;
    private Vector3 vec3;
    private float f1;
    @JsonIgnore
    private int polyIndex;
    private Poly poly;
}
