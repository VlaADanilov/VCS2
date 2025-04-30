package com.technokratos.vcs2.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("vcs.imageresourcepath")
@Data
public class FileSystemProperty {
    private String path = "/vcs";
}
