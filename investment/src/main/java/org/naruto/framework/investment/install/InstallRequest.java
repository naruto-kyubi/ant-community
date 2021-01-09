package org.naruto.framework.investment.install;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstallRequest {
    private String mobileId;
    private String appName;
}
