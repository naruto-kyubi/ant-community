package org.naruto.framework.core.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchItem {
    private String key;
    private String value;
    private String rule;
}
