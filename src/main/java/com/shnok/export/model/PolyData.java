package com.shnok.export.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PolyData {
    private Vector3 origin;
    private Vector3 normal;
    private Vector3 textureU;
    private Vector3 textureV;
    private int vertexCount;
    private List<Vector3> vertices;

}
