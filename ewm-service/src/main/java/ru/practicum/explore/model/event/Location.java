package ru.practicum.explore.model.event;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class Location {

    private float lon;
    private float lat;
}
