package pictobrick.model;

import java.awt.Color;

/**
 * Class for initializing configurations. Code moved from DataManagement by John
 * Watne 09/2023.
 */
public class ConfigurationIntializer {
    /** Gray 6. */
    private static final Color GRAY_6 = new Color(216, 220, 219);
    /** Gray 5. */
    private static final Color GRAY_5 = new Color(198, 205, 209);
    /** Gray 4. */
    private static final Color GRAY_4 = new Color(161, 171, 178);
    /** Gray 3. */
    private static final Color GRAY_3 = new Color(114, 128, 138);
    /** Gray 2. */
    private static final Color GRAY_2 = new Color(73, 88, 101);
    /** Gray 1. */
    private static final Color GRAY_1 = new Color(28, 43, 57);
    /** Corn gold. */
    private static final Color CORN_GOLD = new Color(248, 155, 28);
    /** Flesh. */
    private static final Color FLESH = new Color(251, 193, 159);
    /** Dark gray. */
    private static final Color DARK_GRAY = new Color(121, 124, 130);
    /** Light pink. */
    private static final Color LIGHT_PINK = new Color(245, 154, 169);
    /** Light gray. */
    private static final Color LIGHT_GRAY = new Color(165, 169, 172);
    /** Dark brown. */
    private static final Color DARK_BROWN = new Color(106, 33, 0);
    /** Medium brown. */
    private static final Color MEDIUM_BROWN = new Color(160, 94, 18);
    /** Light brown. */
    private static final Color LIGHT_BROWN = new Color(252, 181, 21);
    /** Beige. */
    private static final Color BEIGE = new Color(255, 233, 143);
    /** Light lime. */
    private static final Color LIGHT_LIME = new Color(197, 225, 170);
    /** Dark tan. */
    private static final Color DARK_TAN = new Color(144, 144, 105);
    /** Light yellow. */
    private static final Color LIGHT_YELLOW = BEIGE;
    /** Yellow. */
    private static final Color YELLOW = new Color(255, 214, 0);
    /** White. */
    private static final Color WHITE = new Color(255, 255, 255);
    /** Very light bluish gray. */
    private static final Color VERY_LIGHT_BLUISH_GRAY = GRAY_5;
    /** Light bluish gray. */
    private static final Color LIGHT_BLUISH_GRAY = GRAY_4;
    /** Dark bluish gray. */
    private static final Color DARK_BLUISH_GRAY = GRAY_2;
    /** Medium lime. */
    private static final Color MEDIUM_LIME = new Color(193, 216, 55);
    /** Lime. */
    private static final Color LIME = new Color(181, 206, 47);
    /** Medium green. */
    private static final Color MEDIUM_GREEN = new Color(151, 211, 184);
    /** Dark green. */
    private static final Color DARK_GREEN = new Color(0, 65, 37);
    /** Aqua. */
    private static final Color AQUA = new Color(162, 218, 222);
    /** Light turquoise. */
    private static final Color LIGHT_TURQUOISE = new Color(0, 179, 176);
    /** Dark turquoise. */
    private static final Color DARK_TURQUOISE = new Color(0, 146, 121);
    /** Light blue. */
    private static final Color LIGHT_BLUE = new Color(193, 224, 244);
    /** Medium blue. */
    private static final Color MEDIUM_BLUE = new Color(94, 174, 224);
    /** Blue. */
    private static final Color BLUE = new Color(0, 87, 164);
    /** Dark blue. */
    private static final Color DARK_BLUE = new Color(0, 54, 96);
    /** Light purple. */
    private static final Color LIGHT_PURPLE = new Color(142, 49, 146);
    /** Purple. */
    private static final Color PURPLE = new Color(127, 63, 152);
    /** Dark purple. */
    private static final Color DARK_PURPLE = new Color(70, 42, 135);
    /** Pink. */
    private static final Color PINK = new Color(247, 179, 204);
    /** Light salmon. */
    private static final Color LIGHT_SALMON = new Color(250, 188, 174);
    /** Salmon. */
    private static final Color SALMON = new Color(246, 150, 124);
    /** Red. */
    private static final Color RED = new Color(238, 46, 36);
    /** Reddish brown. */
    private static final Color REDDISH_BROWN = new Color(104, 46, 47);
    /** 2x1 element. */
    private static final boolean[][] ELEMENT_2X1 = {{true, true}};
    /** 1x2 element. */
    private static final boolean[][] ELEMENT_1X2 = {{true}, {true}};
    /** 3x1 element. */
    private static final boolean[][] ELEMENT_3X1 = {{true, true, true}};
    /** 1x3 element. */
    private static final boolean[][] ELEMENT_1X3 = {{true}, {true}, {true}};
    /** 4x1 element. */
    private static final boolean[][] ELEMENT_4X1 = {{true, true, true, true}};
    /** 1x4 element. */
    private static final boolean[][] ELEMENT_1X4 = {{true}, {true}, {true},
            {true}};
    /** 6x1 element. */
    private static final boolean[][] ELEMENT_6X1 = {
            {true, true, true, true, true, true}};
    /** 1x6 element. */
    private static final boolean[][] ELEMENT_1X6 = {{true}, {true}, {true},
            {true}, {true}, {true}};
    /** 8x1 element. */
    private static final boolean[][] ELEMENT_8X1 = {
            {true, true, true, true, true, true, true, true}};
    /** 1x8 element. */
    private static final boolean[][] ELEMENT_1X8 = {{true}, {true}, {true},
            {true}, {true}, {true}, {true}, {true}};
    /** 2x2 element. */
    private static final boolean[][] ELEMENT_2X2 = {{true, true}, {true, true}};
    /** 2x2 corner element. */
    private static final boolean[][] ELEMENT_2X2_CORNER = {{true, true},
            {true, false}};
    /** 2x2 90 degree corner element. */
    private static final boolean[][] ELEMENT_2X2_CORNER_90 = {{true, true},
            {false, true}};
    /** 2x2 180 degree corner element. */
    private static final boolean[][] ELEMENT_2X2_CORNER_180 = {{false, true},
            {true, true}};
    /** 2x2 270 degree corner element. */
    private static final boolean[][] ELEMENT_2X2_CORNER_270 = {{true, false},
            {true, true}};
    /** 3x2 element. */
    private static final boolean[][] ELEMENT_3X2 = {{true, true, true},
            {true, true, true}};
    /** 2x3 element. */
    private static final boolean[][] ELEMENT_2X3 = {{true, true}, {true, true},
            {true, true}};
    /** 4x2 element. */
    private static final boolean[][] ELEMENT_4X2 = {{true, true, true, true},
            {true, true, true, true}};
    /** 2x4 element. */
    private static final boolean[][] ELEMENT_2X4 = {{true, true}, {true, true},
            {true, true}, {true, true}};
    /** 6x2 element. */
    private static final boolean[][] ELEMENT_6X2 = {
            {true, true, true, true, true, true},
            {true, true, true, true, true, true}};
    /** 2x6 element. */
    private static final boolean[][] ELEMENT_2X6 = {{true, true}, {true, true},
            {true, true}, {true, true}, {true, true}, {true, true}};
    /** 8x2 element. */
    private static final boolean[][] ELEMENT_8X2 = {
            {true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true}};
    /** 2x8 element. */
    private static final boolean[][] ELEMENT_2X8 = {{true, true}, {true, true},
            {true, true}, {true, true}, {true, true}, {true, true},
            {true, true}, {true, true}};
    /** 4x4 element. */
    private static final boolean[][] ELEMENT_4X4 = {{true, true, true, true},
            {true, true, true, true}, {true, true, true, true},
            {true, true, true, true}};
    /** 4x4 corner element. */
    private static final boolean[][] ELEMENT_4X4_CORNER = {
            {true, true, true, true}, {true, true, true, true},
            {true, true, false, false}, {true, true, false, false}};
    /** 4x4 90 degree corner element. */
    private static final boolean[][] ELEMENT_4X4_CORNER_90 = {
            {true, true, true, true}, {true, true, true, true},
            {false, false, true, true}, {false, false, true, true}};
    /** 4x4 180 degree corner element. */
    private static final boolean[][] ELEMENT_4X4_CORNER_180 = {
            {false, false, true, true}, {false, false, true, true},
            {true, true, true, true}, {true, true, true, true}};
    /** 4x4 270 degree corner element. */
    private static final boolean[][] ELEMENT_4X4_CORNER_270 = {
            {true, true, false, false}, {true, true, false, false},
            {true, true, true, true}, {true, true, true, true}};
    /** 6x4 element. */
    private static final boolean[][] ELEMENT_6X4 = {
            {true, true, true, true, true, true},
            {true, true, true, true, true, true},
            {true, true, true, true, true, true},
            {true, true, true, true, true, true}};
    /** 4x6 element. */
    private static final boolean[][] ELEMENT_4X6 = {{true, true, true, true},
            {true, true, true, true}, {true, true, true, true},
            {true, true, true, true}, {true, true, true, true},
            {true, true, true, true}};
    /** 8x4 element. */
    private static final boolean[][] ELEMENT_8X4 = {
            {true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true}};
    /** 4x8 element. */
    private static final boolean[][] ELEMENT_4X8 = {{true, true, true, true},
            {true, true, true, true}, {true, true, true, true},
            {true, true, true, true}, {true, true, true, true},
            {true, true, true, true}, {true, true, true, true},
            {true, true, true, true}};
    /** 6x6 element. */
    private static final boolean[][] ELEMENT_6X6 = {
            {true, true, true, true, true, true},
            {true, true, true, true, true, true},
            {true, true, true, true, true, true},
            {true, true, true, true, true, true},
            {true, true, true, true, true, true},
            {true, true, true, true, true, true}};
    /** 8x6 element. */
    private static final boolean[][] ELEMENT_8X6 = {
            {true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true}};
    /** 6x8 element. */
    private static final boolean[][] ELEMENT_6X8 = {
            {true, true, true, true, true, true},
            {true, true, true, true, true, true},
            {true, true, true, true, true, true},
            {true, true, true, true, true, true},
            {true, true, true, true, true, true},
            {true, true, true, true, true, true},
            {true, true, true, true, true, true},
            {true, true, true, true, true, true}};
    /** 8x8 element. */
    private static final boolean[][] ELEMENT_8X8 = {
            {true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true}};
    /** Int value of 1. */
    private static final int INT1 = 1;
    /** Int value of 3. */
    private static final int INT3 = 3;
    /** Int value of 4. */
    private static final int INT4 = 4;
    /** Olive. */
    private static final Color OLIVE = new Color(76, 55, INT4);
    /** Dark red. */
    private static final Color DARK_RED = new Color(247, INT4, 55);
    /** Int value of 5. */
    private static final int INT5 = 5;
    /** Int value of 6. */
    private static final int INT6 = 6;
    /** Int value of 7. */
    private static final int INT7 = 7;
    /** Int value of 8. */
    private static final int INT8 = 8;
    /** Int value of 9. */
    private static final int INT9 = 9;
    /** Int value of 10. */
    private static final int INT10 = 10;
    /** Int value of 11. */
    private static final int INT11 = 11;
    /** Int value of 12. */
    private static final int INT12 = 12;
    /** Int value of 14. */
    private static final int INT14 = 14;
    /** Int value of 17. */
    private static final int INT17 = 17;
    /** Int value of 23. */
    private static final int INT23 = 23;
    /** Int value of 28. */
    private static final int INT28 = 28;
    /** Medium orange. */
    private static final Color MEDIUM_ORANGE = new Color(48, 155, INT28);
    /** Int value of 32. */
    private static final int INT32 = 32;
    /** Orange. */
    private static final Color ORANGE = new Color(244, 123, INT32);
    /** Int value of 44. */
    private static final int INT44 = 44;
    /** Int value of 73. */
    private static final int INT73 = 73;
    /** Int value of 74. */
    private static final int INT74 = 74;
    /** Green. */
    private static final Color GREEN = new Color(0, 148, INT74);
    /** Int value of 88. */
    private static final int INT88 = 88;
    /** Int value of 101. */
    private static final int INT101 = 101;
    /** Int value of 105. */
    private static final int INT105 = 105;
    /** Int value of 143. */
    private static final int INT143 = 143;
    /** Int value of 144. */
    private static final int INT144 = 144;
    /** Int value of 161. */
    private static final int INT161 = 161;
    /** Int value of 171. */
    private static final int INT171 = 171;
    /** Magenta. */
    private static final Color MAGENTA = new Color(INT171, 29, 137);
    /** Int value of 178. */
    private static final int INT178 = 178;
    /** Int value of 198. */
    private static final int INT198 = 198;
    /** Int value of 205. */
    private static final int INT205 = 205;
    /** Dark pink. */
    private static final Color DARK_PINK = new Color(INT205, 127, 181);
    /** Int value of 209. */
    private static final int INT209 = 209;
    /** Light green. */
    private static final Color LIGHT_GREEN = new Color(186, 225, INT209);
    /** Int value of 214. */
    private static final int INT214 = 214;
    /** Tan color. */
    private static final Color TAN = new Color(INT214, 191, 145);
    /** Int value of 233. */
    private static final int INT233 = 233;
    /** Maximum R, G, or B value in RGB color. */
    private static final int MAX_COLOR_VALUE = 255;

    /** LEGO top view configuration. */
    private final Configuration legoTop;
    /** LEGO side view configuration. */
    private final Configuration legoSide;
    /** Ministeck configuration. */
    private final Configuration ministeck;

    /**
     * Constructor.
     *
     * @param legoTopConfig   LEGO top view configuration.
     * @param legoSideConfig  LEGO side view configuration.
     * @param ministeckConfig Ministeck configuration.
     */
    public ConfigurationIntializer(final Configuration legoTopConfig,
            final Configuration legoSideConfig,
            final Configuration ministeckConfig) {
        this.legoTop = legoTopConfig;
        this.legoSide = legoSideConfig;
        this.ministeck = ministeckConfig;
    }

    /**
     * Creates the system configuration Lego top view.
     *
     * @return the initialized LEGO top view configuration.
     *
     * @author Adrian Schuetz
     */
    public Configuration initConfigurationLegoTop() {
        legoTop.setElement("Plate_2x1_(3023)", 2, 1, ELEMENT_2X1, 1, INT4);
        legoTop.setElement("Plate_1x2_(3023)", 1, 2, ELEMENT_1X2, 1, INT4);
        legoTop.setElement("Plate_3x1_(3623)", INT3, 1, ELEMENT_3X1, 1, INT7);
        legoTop.setElement("Plate_1x3_(3623)", 1, INT3, ELEMENT_1X3, 1, INT7);
        legoTop.setElement("Plate_4x1_(3710)", INT4, 1, ELEMENT_4X1, 1, INT6);
        legoTop.setElement("Plate_1x4_(3710)", 1, INT4, ELEMENT_1X4, 1, INT6);
        legoTop.setElement("Plate_6x1_(3666)", INT6, 1, ELEMENT_6X1, 1, INT7);
        legoTop.setElement("Plate_1x6_(3666)", 1, INT6, ELEMENT_1X6, 1, INT7);
        legoTop.setElement("Plate_8x1_(3460)", INT8, 1, ELEMENT_8X1, 1, INT10);
        legoTop.setElement("Plate_1x8_(3460)", 1, INT8, ELEMENT_1X8, 1, INT10);
        legoTop.setElement("Plate_2x2_Corner_(2420)", 2, 2, ELEMENT_2X2_CORNER,
                1, INT7);
        legoTop.setElement("Plate_2x2_Corner_90_(2420)", 2, 2,
                ELEMENT_2X2_CORNER_90, 1, INT7);
        legoTop.setElement("Plate_2x2_Corner_180_(2420)", 2, 2,
                ELEMENT_2X2_CORNER_180, 1, INT7);
        legoTop.setElement("Plate_2x2_Corner_270_(2420)", 2, 2,
                ELEMENT_2X2_CORNER_270, 1, INT7);
        legoTop.setElement("Plate_2x2_(3022)", 2, 2, ELEMENT_2X2, 1, INT3);
        legoTop.setElement("Plate_3x2_(3021)", INT3, 2, ELEMENT_3X2, 1, INT4);
        legoTop.setElement("Plate_2x3_(3021)", 2, INT3, ELEMENT_2X3, 1, INT4);
        legoTop.setElement("Plate_4x2_(3020)", INT4, 2, ELEMENT_4X2, 1, INT5);
        legoTop.setElement("Plate_2x4_(3020)", 2, INT4, ELEMENT_2X4, 1, INT5);
        legoTop.setElement("Plate_6x2_(3795)", INT6, 2, ELEMENT_6X2, 1, INT7);
        legoTop.setElement("Plate_2x6_(3795)", 2, INT6, ELEMENT_2X6, 1, INT7);
        legoTop.setElement("Plate_8x2_(3024)", INT8, 2, ELEMENT_8X2, 1, INT9);
        legoTop.setElement("Plate_2x8_(3024)", 2, INT8, ELEMENT_2X8, 1, INT9);
        legoTop.setElement("Plate_4x4_(3031)", INT4, INT4, ELEMENT_4X4, 1,
                INT10);
        legoTop.setElement("Plate_4x4_Corner_(2639)", INT4, INT4,
                ELEMENT_4X4_CORNER, 1, INT32);
        legoTop.setElement("Plate_4x4_Corner_90_(2639)", INT4, INT4,
                ELEMENT_4X4_CORNER_90, 1, INT32);
        legoTop.setElement("Plate_4x4_Corner_180_(2639)", INT4, INT4,
                ELEMENT_4X4_CORNER_180, 1, INT32);
        legoTop.setElement("Plate_4x4_Corner_270_(2639)", INT4, INT4,
                ELEMENT_4X4_CORNER_270, 1, INT32);
        legoTop.setElement("Plate_6x4_(3032)", INT6, INT4, ELEMENT_6X4, 1,
                INT17);
        legoTop.setElement("Plate_4x6_(3032)", INT4, INT6, ELEMENT_4X6, 1,
                INT17);
        legoTop.setElement("Plate_8x4_(3035)", INT8, INT4, ELEMENT_8X4, 1,
                INT23);
        legoTop.setElement("Plate_4x8_(3035)", INT4, INT8, ELEMENT_4X8, 1,
                INT23);
        legoTop.setElement("Plate_6x6_(3958)", INT6, INT6, ELEMENT_6X6, 1,
                INT28);
        legoTop.setElement("Plate_8x6_(3036)", INT8, INT6, ELEMENT_8X6, 1,
                INT44);
        legoTop.setElement("Plate_6x8_(3036)", INT6, INT8, ELEMENT_6X8, 1,
                INT44);
        legoTop.setElement("Plate_8x8_(41539)", INT8, INT8, ELEMENT_8X8, 1,
                INT74);
        legoTop.setColor("11_Black", 0, 0, 0);
        legoTop.setColor("85_Dark_Bluish_Gray", INT73, INT88, INT101);
        legoTop.setColor("86_Light_Bluish_Gray", INT161, INT171, INT178);
        legoTop.setColor("99_Very_Light_Bluish_Gray", INT198, INT205, INT209);
        legoTop.setColor("01_White", MAX_COLOR_VALUE, MAX_COLOR_VALUE,
                MAX_COLOR_VALUE);
        legoTop.setColor("03_Yellow", MAX_COLOR_VALUE, INT214, 0);
        legoTop.setColor("33_Light_Yellow", MAX_COLOR_VALUE, INT233, INT143);
        legoTop.setColor("69_Dark_Tan", INT144, INT144, INT105);
        legoTop.setColor("02_Tan", TAN);
        legoTop.setColor("88_Reddish_Brown", REDDISH_BROWN);
        legoTop.setColor("04_Orange", ORANGE);
        legoTop.setColor("31_Medium_Orange", MEDIUM_ORANGE);
        legoTop.setColor("59_Dark_Red", DARK_RED);
        legoTop.setColor("05_Red", RED);
        legoTop.setColor("25_Salmon", SALMON);
        legoTop.setColor("26_Light_Salmon", LIGHT_SALMON);
        legoTop.setColor("47_Dark_Pink", DARK_PINK);
        legoTop.setColor("23_Pink", PINK);
        legoTop.setColor("89_Dark_Purple", DARK_PURPLE);
        legoTop.setColor("24_Purple", PURPLE);
        legoTop.setColor("93_Light_Purple", LIGHT_PURPLE);
        legoTop.setColor("71_Magenta", MAGENTA);
        legoTop.setColor("63_Dark_Blue", DARK_BLUE);
        legoTop.setColor("07_Blue", BLUE);
        legoTop.setColor("42_Medium_Blue", MEDIUM_BLUE);
        legoTop.setColor("62_Light_Blue", LIGHT_BLUE);
        legoTop.setColor("39_Dark_Turquoise", DARK_TURQUOISE);
        legoTop.setColor("40_Light_Turquoise", LIGHT_TURQUOISE);
        legoTop.setColor("41_Aqua", AQUA);
        legoTop.setColor("80_Dark_Green", DARK_GREEN);
        legoTop.setColor("06_Green", GREEN);
        legoTop.setColor("37_Medium_Green", MEDIUM_GREEN);
        legoTop.setColor("38_Light_Green", LIGHT_GREEN);
        legoTop.setColor("34_Lime", LIME);
        legoTop.setColor("76_Medium_Lime", MEDIUM_LIME);
        return legoTop;
    }

    /**
     * Creates the system configuration Lego side view.
     *
     * @author Adrian Schuetz
     * @return the initialized LEGO side view configuration.
     */
    public Configuration initConfigurationLegoSide() {
        final boolean[][] element2x1 = {{true, true}};
        final boolean[][] element3x1 = {{true, true, true}};
        final boolean[][] element4x1 = {{true, true, true, true}};
        final boolean[][] element6x1 = {{true, true, true, true, true, true}};
        final boolean[][] element8x1 = {
                {true, true, true, true, true, true, true, true}};
        final boolean[][] element1x3 = {{true}, {true}, {true}};
        final boolean[][] element2x3 = {{true, true}, {true, true},
                {true, true}};
        final boolean[][] element3x3 = {{true, true, true}, {true, true, true},
                {true, true, true}};
        final boolean[][] element4x3 = {{true, true, true, true},
                {true, true, true, true}, {true, true, true, true},
                {true, true, true, true}};
        final boolean[][] element6x3 = {{true, true, true, true, true, true},
                {true, true, true, true, true, true},
                {true, true, true, true, true, true}};
        final boolean[][] element8x3 = {
                {true, true, true, true, true, true, true, true},
                {true, true, true, true, true, true, true, true},
                {true, true, true, true, true, true, true, true}};
        legoSide.setElement("Plate_1x2_(3023)", 2, 1, element2x1, INT8, INT4);
        legoSide.setElement("Plate_1x3_(3623)", INT3, 1, element3x1, INT9,
                INT7);
        legoSide.setElement("Plate_1x4_(3710)", INT4, INT1, element4x1, INT10,
                INT6);
        legoSide.setElement("Plate_1x6_(3666)", INT6, 1, element6x1, INT11,
                INT7);
        legoSide.setElement("Plate_1x8_(3460)", INT8, 1, element8x1, INT12,
                INT10);
        legoSide.setElement("Brick_1x1_(3005)", 1, INT3, element1x3, 1, 2);
        legoSide.setElement("Brick_1x2_(3004)", 2, INT3, element2x3, 2, 2);
        legoSide.setElement("Brick_1x3_(3622)", INT3, INT3, element3x3, INT3,
                INT4);
        legoSide.setElement("Brick_1x4_(3010)", INT4, INT3, element4x3, INT4,
                INT4);
        legoSide.setElement("Brick_1x6_(3009)", INT6, INT3, element6x3, INT5,
                INT7);
        legoSide.setElement("Brick_1x8_(3008)", INT8, INT3, element8x3, INT6,
                INT14);
        legoSide.setColor("11_Black", 0, 0, 0);
        legoSide.setColor("85_Dark_Bluish_Gray", DARK_BLUISH_GRAY);
        legoSide.setColor("86_Light_Bluish_Gray", LIGHT_BLUISH_GRAY);
        legoSide.setColor("99_Very_Light_Bluish_Gray", VERY_LIGHT_BLUISH_GRAY);
        legoSide.setColor("01_White", WHITE);
        legoSide.setColor("03_Yellow", YELLOW);
        legoSide.setColor("33_Light_Yellow", LIGHT_YELLOW);
        legoSide.setColor("69_Dark_Tan", DARK_TAN);
        legoSide.setColor("02_Tan", TAN);
        legoSide.setColor("88_Reddish_Brown", REDDISH_BROWN);
        legoSide.setColor("04_Orange", ORANGE);
        legoSide.setColor("31_Medium_Orange", MEDIUM_ORANGE);
        legoSide.setColor("59_Dark_Red", DARK_RED);
        legoSide.setColor("05_Red", RED);
        legoSide.setColor("25_Salmon", SALMON);
        legoSide.setColor("26_Light_Salmon", LIGHT_SALMON);
        legoSide.setColor("47_Dark_Pink", DARK_PINK);
        legoSide.setColor("23_Pink", PINK);
        legoSide.setColor("89_Dark_Purple", DARK_PURPLE);
        legoSide.setColor("24_Purple", PURPLE);
        legoSide.setColor("93_Light_Purple", LIGHT_PURPLE);
        legoSide.setColor("71_Magenta", MAGENTA);
        legoSide.setColor("63_Dark_Blue", DARK_BLUE);
        legoSide.setColor("07_Blue", BLUE);
        legoSide.setColor("42_Medium_Blue", MEDIUM_BLUE);
        legoSide.setColor("62_Light_Blue", LIGHT_BLUE);
        legoSide.setColor("39_Dark_Turquoise", DARK_TURQUOISE);
        legoSide.setColor("40_Light_Turquoise", LIGHT_TURQUOISE);
        legoSide.setColor("41_Aqua", AQUA);
        legoSide.setColor("80_Dark_Green", DARK_GREEN);
        legoSide.setColor("06_Green", GREEN);
        legoSide.setColor("37_Medium_Green", MEDIUM_GREEN);
        legoSide.setColor("38_Light_Green", LIGHT_GREEN);
        legoSide.setColor("34_Lime", LIME);
        legoSide.setColor("76_Medium_Lime", MEDIUM_LIME);
        legoSide.setColor("35_Light_Lime", LIGHT_LIME);
        return legoSide;
    }

    /**
     * Creates the system configuration Ministeck.
     *
     * @author Adrian Schuetz
     * @return the initialized Ministeck configuration.
     */
    public Configuration initConfigurationMinisteck() {
        final boolean[][] element2x1 = {{true, true}};
        final boolean[][] element1x2 = {{true}, {true}};
        final boolean[][] element3x1 = {{true, true, true}};
        final boolean[][] element1x3 = {{true}, {true}, {true}};
        final boolean[][] element2x2 = {{true, true}, {true, true}};
        final boolean[][] element2x2Corner = {{true, true}, {true, false}};
        final boolean[][] element2x2Corner90 = {{true, true}, {false, true}};
        final boolean[][] element2x2Corner180 = {{false, true}, {true, true}};
        final boolean[][] element2x2Corner270 = {{true, false}, {true, true}};
        ministeck.setElement("Ministeck_2x1", 2, 1, element2x1, 1, 1);
        ministeck.setElement("Ministeck_1x2", 1, 2, element1x2, 1, 1);
        ministeck.setElement("Ministeck_3x1", INT3, 1, element3x1, 1, 1);
        ministeck.setElement("Ministeck_1x3", 1, INT3, element1x3, 1, 1);
        ministeck.setElement("Ministeck_2x2_Corner", 2, 2, element2x2Corner, 1,
                1);
        ministeck.setElement("Ministeck_2x2_Corner_90", 2, 2,
                element2x2Corner90, 1, 1);
        ministeck.setElement("Ministeck_2x2_Corner_180", 2, 2,
                element2x2Corner180, 1, 1);
        ministeck.setElement("Ministeck_2x2_Corner_270", 2, 2,
                element2x2Corner270, 1, 1);
        ministeck.setElement("Ministeck_2x2", 2, 2, element2x2, 1, 1);
        ministeck.setColor("601_Black", 0, 0, 0);
        ministeck.setColor("602_Dark_Blue", DARK_BLUE);
        ministeck.setColor("624_Medium_Blue", MEDIUM_BLUE);
        ministeck.setColor("621_Medium_Green", MEDIUM_GREEN);
        ministeck.setColor("605_Dark_Green", DARK_GREEN);
        ministeck.setColor("606_Red", RED);
        ministeck.setColor("607_Orange", ORANGE);
        ministeck.setColor("608_Yellow", YELLOW);
        ministeck.setColor("609_Beige", BEIGE);
        ministeck.setColor("610_Light_Brown", LIGHT_BROWN);
        ministeck.setColor("611_Medium_Brown", MEDIUM_BROWN);
        ministeck.setColor("612_Dark_Brown", DARK_BROWN);
        ministeck.setColor("613_White", WHITE);
        ministeck.setColor("614_Light_Grey", LIGHT_GRAY);
        ministeck.setColor("615_Light_Pink", LIGHT_PINK);
        ministeck.setColor("616_Dark_Grey", DARK_GRAY);
        ministeck.setColor("617_Olive", OLIVE);
        ministeck.setColor("619_Flesh", FLESH);
        ministeck.setColor("620_Purple", PURPLE);
        ministeck.setColor("604_Light_Green", LIGHT_GREEN);
        ministeck.setColor("622_Corn_Gold", CORN_GOLD);
        ministeck.setColor("623_Pink", PINK);
        ministeck.setColor("603_Light_Blue", LIGHT_BLUE);
        ministeck.setColor("635_Grey_1", GRAY_1);
        ministeck.setColor("636_Grey_2", GRAY_2);
        ministeck.setColor("637_Grey_3", GRAY_3);
        ministeck.setColor("638_Grey_4", GRAY_4);
        ministeck.setColor("639_Grey_5", GRAY_5);
        ministeck.setColor("641_Grey_6", GRAY_6);
        return ministeck;
    }

}
