package com.sb.solutions.report.core.util;

import static ar.com.fdvs.dj.domain.constants.Border.NO_BORDER;
import static java.awt.Color.LIGHT_GRAY;

import java.awt.Color;

import lombok.experimental.UtilityClass;

import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;


/**
 * @author Sunil Babu Shrestha on 4/24/2020
 */
@UtilityClass
public class StyleUtil {

    public static Style headerStyle() {
        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.gray);
        headerStyle.setTextColor(Color.white);
        headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);
        return headerStyle;
    }

    public static Style headerVairalbleStyle() {
        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerVariables.setBackgroundColor(Color.pink);
        return headerVariables;
    }

    public static Style detailStyle() {

        return new Style();
    }


    public static Style oddRowStyle() {
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(NO_BORDER());
        oddRowStyle.setBackgroundColor(LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);
        return oddRowStyle;
    }

    public static Style titleStyle() {
        Style titleStyle = new Style();
        titleStyle.setFont(new Font(18, Font._FONT_VERDANA, true));
        return titleStyle;
    }

    public static Style numberStyle() {
        Style numberStyle = new Style();
        numberStyle.setHorizontalAlign(HorizontalAlign.RIGHT);
        return numberStyle;
    }
}
