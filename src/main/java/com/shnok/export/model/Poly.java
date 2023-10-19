package com.shnok.export.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Poly {

    private String name;
    private int polyCount;
    private List<PolyData> polyData;
}
