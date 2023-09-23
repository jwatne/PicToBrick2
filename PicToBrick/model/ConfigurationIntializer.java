package pictobrick.model;

/**
 * Class for initializing configurations. Code moved from DataManagement by John
 * Watne 09/2023.
 */
public class ConfigurationIntializer {
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
    /** Int value of 3. */
    private static final int INT3 = 3;
    /** Int value of 4. */
    private static final int INT4 = 4;
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
    /** Int value of 17. */
    private static final int INT17 = 17;
    /** Int value of 23. */
    private static final int INT23 = 23;
    /** Int value of 28. */
    private static final int INT28 = 28;
    /** Int value of 32. */
    private static final int INT32 = 32;
    /** Int value of 44. */
    private static final int INT44 = 44;
    /** Int value of 73. */
    private static final int INT73 = 73;
    /** Int value of 74. */
    private static final int INT74 = 74;
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
    /** Int value of 178. */
    private static final int INT178 = 178;
    /** Int value of 198. */
    private static final int INT198 = 198;
    /** Int value of 205. */
    private static final int INT205 = 205;
    /** Int value of 209. */
    private static final int INT209 = 209;
    /** Int value of 214. */
    private static final int INT214 = 214;
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
        legoTop.setColor("02_Tan", INT214, 191, 145);
        legoTop.setColor("88_Reddish_Brown", 104, 46, 47);
        legoTop.setColor("04_Orange", 244, 123, INT32);
        legoTop.setColor("31_Medium_Orange", 248, 155, INT28);
        legoTop.setColor("59_Dark_Red", 247, INT4, 55);
        legoTop.setColor("05_Red", 238, 46, 36);
        legoTop.setColor("25_Salmon", 246, 150, 124);
        legoTop.setColor("26_Light_Salmon", 250, 188, 174);
        legoTop.setColor("47_Dark_Pink", INT205, 127, 181);
        legoTop.setColor("23_Pink", 247, 179, 204);
        legoTop.setColor("89_Dark_Purple", 70, 42, 135);
        legoTop.setColor("24_Purple", 127, 63, 152);
        legoTop.setColor("93_Light_Purple", 142, 49, 146);
        legoTop.setColor("71_Magenta", INT171, 29, 137);
        legoTop.setColor("63_Dark_Blue", 0, 54, 96);
        legoTop.setColor("07_Blue", 0, 87, 164);
        legoTop.setColor("42_Medium_Blue", 94, 174, 224);
        legoTop.setColor("62_Light_Blue", 193, 224, 244);
        legoTop.setColor("39_Dark_Turquoise", 0, 146, 121);
        legoTop.setColor("40_Light_Turquoise", 0, 179, 176);
        legoTop.setColor("41_Aqua", 162, 218, 222);
        legoTop.setColor("80_Dark_Green", 0, 65, 37);
        legoTop.setColor("06_Green", 0, 148, INT74);
        legoTop.setColor("37_Medium_Green", 151, 211, 184);
        legoTop.setColor("38_Light_Green", 186, 225, INT209);
        legoTop.setColor("34_Lime", 181, 206, 47);
        legoTop.setColor("76_Medium_Lime", 193, 216, 55);
        return legoTop;
    }

    /**
     * Creates the system configuration Lego side view.
     *
     * @author Adrian Schuetz
     * @return the initialized LEGO side view configuration.
     */
    public Configuration initConfigurationLegoSide() {
        final boolean[][] element_2x1 = {{true, true}};
        final boolean[][] element_3x1 = {{true, true, true}};
        final boolean[][] element_4x1 = {{true, true, true, true}};
        final boolean[][] element_6x1 = {{true, true, true, true, true, true}};
        final boolean[][] element_8x1 = {
                {true, true, true, true, true, true, true, true}};
        final boolean[][] element_1x3 = {{true}, {true}, {true}};
        final boolean[][] element_2x3 = {{true, true}, {true, true},
                {true, true}};
        final boolean[][] element_3x3 = {{true, true, true}, {true, true, true},
                {true, true, true}};
        final boolean[][] element_4x3 = {{true, true, true, true},
                {true, true, true, true}, {true, true, true, true},
                {true, true, true, true}};
        final boolean[][] element_6x3 = {{true, true, true, true, true, true},
                {true, true, true, true, true, true},
                {true, true, true, true, true, true}};
        final boolean[][] element_8x3 = {
                {true, true, true, true, true, true, true, true},
                {true, true, true, true, true, true, true, true},
                {true, true, true, true, true, true, true, true}};
        legoSide.setElement("Plate_1x2_(3023)", 2, 1, element_2x1, 8, 4);
        legoSide.setElement("Plate_1x3_(3623)", 3, 1, element_3x1, 9, 7);
        legoSide.setElement("Plate_1x4_(3710)", 4, 1, element_4x1, 10, 6);
        legoSide.setElement("Plate_1x6_(3666)", 6, 1, element_6x1, 11, 7);
        legoSide.setElement("Plate_1x8_(3460)", 8, 1, element_8x1, 12, 10);
        legoSide.setElement("Brick_1x1_(3005)", 1, 3, element_1x3, 1, 2);
        legoSide.setElement("Brick_1x2_(3004)", 2, 3, element_2x3, 2, 2);
        legoSide.setElement("Brick_1x3_(3622)", 3, 3, element_3x3, 3, 4);
        legoSide.setElement("Brick_1x4_(3010)", 4, 3, element_4x3, 4, 4);
        legoSide.setElement("Brick_1x6_(3009)", 6, 3, element_6x3, 5, 7);
        legoSide.setElement("Brick_1x8_(3008)", 8, 3, element_8x3, 6, 14);
        legoSide.setColor("11_Black", 0, 0, 0);
        legoSide.setColor("85_Dark_Bluish_Gray", 73, 88, 101);
        legoSide.setColor("86_Light_Bluish_Gray", 161, 171, 178);
        legoSide.setColor("99_Very_Light_Bluish_Gray", 198, 205, 209);
        legoSide.setColor("01_White", 255, 255, 255);
        legoSide.setColor("03_Yellow", 255, 214, 0);
        legoSide.setColor("33_Light_Yellow", 255, 233, 143);
        legoSide.setColor("69_Dark_Tan", 144, 144, 105);
        legoSide.setColor("02_Tan", 214, 191, 145);
        legoSide.setColor("88_Reddish_Brown", 104, 46, 47);
        legoSide.setColor("04_Orange", 244, 123, 32);
        legoSide.setColor("31_Medium_Orange", 248, 155, 28);
        legoSide.setColor("59_Dark_Red", 247, 4, 55);
        legoSide.setColor("05_Red", 238, 46, 36);
        legoSide.setColor("25_Salmon", 246, 150, 124);
        legoSide.setColor("26_Light_Salmon", 250, 188, 174);
        legoSide.setColor("47_Dark_Pink", 205, 127, 181);
        legoSide.setColor("23_Pink", 247, 179, 204);
        legoSide.setColor("89_Dark_Purple", 70, 42, 135);
        legoSide.setColor("24_Purple", 127, 63, 152);
        legoSide.setColor("93_Light_Purple", 142, 49, 146);
        legoSide.setColor("71_Magenta", 171, 29, 137);
        legoSide.setColor("63_Dark_Blue", 0, 54, 96);
        legoSide.setColor("07_Blue", 0, 87, 164);
        legoSide.setColor("42_Medium_Blue", 94, 174, 224);
        legoSide.setColor("62_Light_Blue", 193, 224, 244);
        legoSide.setColor("39_Dark_Turquoise", 0, 146, 121);
        legoSide.setColor("40_Light_Turquoise", 0, 179, 176);
        legoSide.setColor("41_Aqua", 162, 218, 222);
        legoSide.setColor("80_Dark_Green", 0, 65, 37);
        legoSide.setColor("06_Green", 0, 148, 74);
        legoSide.setColor("37_Medium_Green", 151, 211, 184);
        legoSide.setColor("38_Light_Green", 186, 225, 209);
        legoSide.setColor("34_Lime", 181, 206, 47);
        legoSide.setColor("76_Medium_Lime", 193, 216, 55);
        legoSide.setColor("35_Light_Lime", 197, 225, 170);
        return legoSide;
    }

    /**
     * Creates the system configuration Ministeck.
     *
     * @author Adrian Schuetz
     * @return the initialized Ministeck configuration.
     */
    public Configuration initConfigurationMinisteck() {
        final boolean[][] element_2x1 = {{true, true}};
        final boolean[][] element_1x2 = {{true}, {true}};
        final boolean[][] element_3x1 = {{true, true, true}};
        final boolean[][] element_1x3 = {{true}, {true}, {true}};
        final boolean[][] element_2x2 = {{true, true}, {true, true}};
        final boolean[][] element_2x2_Corner = {{true, true}, {true, false}};
        final boolean[][] element_2x2_Corner_90 = {{true, true}, {false, true}};
        final boolean[][] element_2x2_Corner_180 = {{false, true},
                {true, true}};
        final boolean[][] element_2x2_Corner_270 = {{true, false},
                {true, true}};
        ministeck.setElement("Ministeck_2x1", 2, 1, element_2x1, 1, 1);
        ministeck.setElement("Ministeck_1x2", 1, 2, element_1x2, 1, 1);
        ministeck.setElement("Ministeck_3x1", 3, 1, element_3x1, 1, 1);
        ministeck.setElement("Ministeck_1x3", 1, 3, element_1x3, 1, 1);
        ministeck.setElement("Ministeck_2x2_Corner", 2, 2, element_2x2_Corner,
                1, 1);
        ministeck.setElement("Ministeck_2x2_Corner_90", 2, 2,
                element_2x2_Corner_90, 1, 1);
        ministeck.setElement("Ministeck_2x2_Corner_180", 2, 2,
                element_2x2_Corner_180, 1, 1);
        ministeck.setElement("Ministeck_2x2_Corner_270", 2, 2,
                element_2x2_Corner_270, 1, 1);
        ministeck.setElement("Ministeck_2x2", 2, 2, element_2x2, 1, 1);
        ministeck.setColor("601_Black", 0, 0, 0);
        ministeck.setColor("602_Dark_Blue", 36, 60, 150);
        ministeck.setColor("624_Medium_Blue", 134, 187, 229);
        ministeck.setColor("621_Medium_Green", 67, 182, 73);
        ministeck.setColor("605_Dark_Green", 0, 100, 62);
        ministeck.setColor("606_Red", 169, 17, 44);
        ministeck.setColor("607_Orange", 242, 103, 36);
        ministeck.setColor("608_Yellow", 255, 242, 3);
        ministeck.setColor("609_Beige", 255, 233, 143);
        ministeck.setColor("610_Light_Brown", 252, 181, 21);
        ministeck.setColor("611_Medium_Brown", 160, 94, 18);
        ministeck.setColor("612_Dark_Brown", 106, 33, 0);
        ministeck.setColor("613_White", 255, 255, 255);
        ministeck.setColor("614_Light_Grey", 165, 169, 172);
        ministeck.setColor("615_Light_Pink", 245, 154, 169);
        ministeck.setColor("616_Dark_Grey", 121, 124, 130);
        ministeck.setColor("617_Olive", 76, 55, 4);
        ministeck.setColor("619_Flesh", 251, 193, 159);
        ministeck.setColor("620_Purple", 91, 24, 106);
        ministeck.setColor("604_Light_Green", 0, 169, 79);
        ministeck.setColor("622_Corn_Gold", 248, 155, 28);
        ministeck.setColor("623_Pink", 234, 54, 146);
        ministeck.setColor("603_Light_Blue", 0, 111, 186);
        ministeck.setColor("635_Grey_1", 28, 43, 57);
        ministeck.setColor("636_Grey_2", 73, 88, 101);
        ministeck.setColor("637_Grey_3", 114, 128, 138);
        ministeck.setColor("638_Grey_4", 161, 171, 178);
        ministeck.setColor("639_Grey_5", 198, 205, 209);
        ministeck.setColor("641_Grey_6", 216, 220, 219);
        return ministeck;
    }

}
