package org.naruto.framework.investment.install;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppInfo {
    private String appPackage;
    private String appActivity;
    private String apkName;
}
