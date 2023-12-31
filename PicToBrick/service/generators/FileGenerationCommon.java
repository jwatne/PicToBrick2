package pictobrick.service.generators;

import java.util.ResourceBundle;

import pictobrick.service.DataProcessor;

/**
 * Common code for file generating classes. Code moved from OutputFileGenerator
 * by John Watne 10/2023.
 */
public class FileGenerationCommon {
    /** Text resource bundle. */
    private static ResourceBundle textbundle = ResourceBundle
            .getBundle("Resources.TextResource");
    /** 1/2 length and width of 1x1 tile in pixels; used to get midpoint. */
    static final int HALF_TILE = 4;

    // ############################## all
    /** Header. */
    public static final String HEADER = """
            <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN"
            "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
            <html xmlns="http://www.w3.org/1999/xhtml">
            <head>
            <title>pictobrick</title>
            <meta http-equiv="content-type"
             content="text/html; charset=utf-8" />
            <style type="text/css">
            #menu {
                left:0;
                top:10;
                width:200px;
                position:absolute;
                padding-left:10px;
                padding-right:10px;
                padding-top:0px;
                margin-left:0;
                margin-top:0;
                margin-right:0;
                font-family:Verdana, Arial;
                font-size:small;
                }
                #content {
                margin-top:0px;
                margin-left:200px;
                margin-right:0px;
                padding-left:10px;
                padding-right:10px;
                padding-top:0px;
                font-family:Verdana, Arial;
                font-size:small;
                }
                #headline {
                font-size:x-large;
                }
                a:link {
                text-decoration:none;
                color:blue;
                }
                a:visited {
                text-decoration:none;
                color:blue;
                }
                a:hover {
                text-decoration:underline;
                color:blue;
                }
                a:active {
                text-decoration:underline;
                color:blue;
                }
                td {
                padding-right:10px;
                padding-left:10px;
                }
                </style>
                </head>
                <body>
                            """;
    /** Project name. */
    static final String PROJECT_NAME = "</ul>\r\n</div>\r\n"
            + "<div id=\"content\">\r\n<p>\r\n<strong>pictobrick - "
            + textbundle.getString("output_outputFiles_1")
            + "</strong>\r\n</p>\r\n<p id=\"headline\">\r\n<strong>"
            + textbundle.getString("output_outputFiles_2") + ": ";
    /** End of page. */
    static final String END = "<br />\r\n</div>\r\n</body>\r\n</html>";

    /** Project. */
    private String project;
    /** Data processor. */
    private final DataProcessor dataProcessing;
    /** Reference value. */
    private int referenceValue;
    /** Percent generated. */
    private int percent = 0;

    /**
     * Returns percent generated.
     *
     * @return percent generated.
     */
    public int getPercent() {
        return percent;
    }

    /**
     * Sets percent generated.
     *
     * @param percentGenerated percent generated.
     */
    public void setPercent(final int percentGenerated) {
        this.percent = percentGenerated;
    }

    /**
     * Constructor.
     *
     * @param processor data processor.
     */
    public FileGenerationCommon(final DataProcessor processor) {
        this.dataProcessing = processor;
    }

    /**
     * Returns data processor.
     *
     * @return data processor.
     */
    public DataProcessor getDataProcessing() {
        return dataProcessing;
    }

    /**
     * Returns text resource bundle.
     *
     * @return text resource bundle.
     */
    public static ResourceBundle getTextbundle() {
        return textbundle;
    }

    /**
     * Returns project.
     *
     * @return project.
     */
    public String getProject() {
        return project;
    }

    /**
     * Sets the projects name.
     *
     * @author Tobias Reichling
     * @param name
     */
    public void setProject(final String name) {
        this.project = name;
    }

    /**
     * Get the common document start for all pages generated.
     *
     * @param generateGraphics <code>true</code> if generating graphics output.
     * @param configuration    <code>true</code> if generating configuration
     *                         output.
     * @param material         <code>true</code> if generating material output.
     * @param instruction      <code>true</code> if generating instructions.
     * @param xml              <code>true</code> if generating XML.
     * @return a StringBuilder containing the common document starting text.
     */
    StringBuilder getCommonDocumentStart(final boolean generateGraphics,
            final boolean configuration, final boolean material,
            final boolean instruction, final boolean xml) {
        final StringBuilder builder = new StringBuilder();
        builder.append(FileGenerationCommon.HEADER);
        builder.append(OutputFileGenerator.START_MENU);

        if (generateGraphics) {
            builder.append(OutputFileGenerator.GRAPHIC_MENU);
        }

        if (configuration) {
            builder.append(OutputFileGenerator.CONFIGURATION_MENU);
        }

        if (material) {
            builder.append(OutputFileGenerator.MATERIAL_MENU);
        }

        if (instruction) {
            builder.append(OutputFileGenerator.INSTRUCTION_MENU);
        }

        if (xml) {
            builder.append(OutputFileGenerator.XML_MENU);
        }

        builder.append(OutputFileGenerator.ADDITIONAL_MENU);
        builder.append(FileGenerationCommon.PROJECT_NAME);
        builder.append(getProject());
        builder.append(OutputFileGenerator.PROJECT_END);
        return builder;
    }

    /**
     * Returns reference value.
     *
     * @return reference value.
     */
    public int getReferenceValue() {
        return referenceValue;
    }

    /**
     * Sets reference value.
     *
     * @param refValue reference value.
     */
    public final void setReferenceValue(final int refValue) {
        this.referenceValue = refValue;
    }

}
