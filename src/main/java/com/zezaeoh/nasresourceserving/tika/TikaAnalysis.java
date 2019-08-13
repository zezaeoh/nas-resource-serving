package com.zezaeoh.nasresourceserving.tika;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.Tika;

public class TikaAnalysis {
    public static String detectDocTypeUsingFacade(InputStream stream) throws IOException {
        Tika tika = new Tika();
        String mediaType = tika.detect(stream);
        return mediaType;
    }
}