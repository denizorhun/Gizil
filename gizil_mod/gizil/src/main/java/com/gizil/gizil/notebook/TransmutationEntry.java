package com.gizil.gizil.notebook;

public record TransmutationEntry(
        String circle,
        String inputItem, int inputCount,
        String outputItem, int outputCount
) {}