package pictobrick.service.generators;

import java.util.List;
import java.util.Vector;

import javax.swing.SwingUtilities;

import pictobrick.service.DataProcessor;
import pictobrick.ui.ProgressBarsAlgorithms;
import pictobrick.ui.ProgressBarsOutputFiles;

/**
 * XML file generator. Code moved from OutputFileGenerator by John Watne
 * 10/2023.
 */
public class XmlGenerator {
    // ############################## xml.html
    // ---------->all:head
    // ---------->all without index: from menu_start till project_end
    /** All XML. */
    private static final String XML_ALL = "<p>\r\n<strong>"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_10")
            + "</strong><br />\r\n</p>\r\n<p>\r\n"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_18")
            + "\r\n</p>\r\n<p>\r\n<a href=\"mosaic.xml\">\r\n"
            + FileGenerationCommon.getTextbundle()
                    .getString("output_outputFiles_37")
            + "\r\n</a>\r\n</p>";
    // ---------->all:end

    // ############################## mosaic.xml
    /** XML document start 1. */
    private static final String XML_DOCUMENT_START1 = "<?xml version=\""
            + "1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n"
            + "<project name=\"";
    // ---------->projectname
    /** XMLdocument start 2. */
    private static final String XML_DOCUMENT_START2 = "\">";
    /** XML document row start. */
    private static final String XML_DOCUMENT_ROW_START = "<row>\r\n";
    /** XML document row start. */
    private static final String XML_DOCUMENT_ELEMENT_START = "<unit>\r\n"
            + "<element>\r\n";
    // ---------->element
    /** XML document element. */
    private static final String XML_DOCUMENT_ELEMENT_CENTER = "\r\n"
            + "</element>\r\n<color>\r\n";
    // ---------->color
    /** XML document end. */
    private static final String XML_DOCUMENT_ELEMENT_END = "\r\n</color>\r\n"
            + "</unit>\r\n";
    /** XML document element empty. */
    private static final String XML_DOCUMENT_ELEMENT_EMPTY = "<unit />\r\n";
    /** XML document row end. */
    private static final String XML_DOCUMENT_ROW_END = "</row>\r\n";
    /** XML document end. */
    private static final String XML_DOCUMENT_END = "</project>\r\n";
    /** Common file generating code. */
    private final FileGenerationCommon common;

    /**
     * Constructor.
     *
     * @param fileGenerationCommon common file generating code.
     */
    public XmlGenerator(final FileGenerationCommon fileGenerationCommon) {
        this.common = fileGenerationCommon;
    }

    /**
     * Generates XML file, if requested.
     *
     * @param generateGraphics <code>true</code> if generating graphics.
     * @param configuration    <code>true</code> if generating configuration.
     * @param material         <code>true</code> if generating bill of
     *                         materials.
     * @param instruction      <code>true</code> if generating instructions.
     * @param xml              <code>true</code> if generating XML.
     * @param initialError     any error text from previous file generation
     *                         steps.
     * @return updated error text.
     */
    String generateXml(final boolean generateGraphics,
            final boolean configuration, final boolean material,
            final boolean instruction, final boolean xml,
            final String initialError) {
        String error = initialError;
        final DataProcessor dataProcessing = common.getDataProcessing();

        if (xml) {
            List<List<Vector<String>>> mosaicMatrix = dataProcessing
                    .getMosaic();
            final StringBuilder xmlString = common.getCommonDocumentStart(
                    generateGraphics, configuration, material, instruction,
                    xml);
            xmlString.append(XML_ALL);
            xmlString.append(FileGenerationCommon.END);

            if (!(dataProcessing.generateUTFOutput(xmlString.toString(),
                    "xml.html", true))) {
                error = error + "xml.html " + FileGenerationCommon
                        .getTextbundle().getString("output_outputFiles_38")
                        + ".\n\r";
            }

            // mosaic.xml
            final StringBuilder xmldocumentString = new StringBuilder();
            xmldocumentString.append(XML_DOCUMENT_START1);
            xmldocumentString.append(common.getProject());
            xmldocumentString.append(XML_DOCUMENT_START2);
            mosaicMatrix = dataProcessing.getMosaic();
            common.setPercent(0);
            common.setReferenceValue(FileGenerationCommon.HALF_TILE);
            common.setReferenceValue((dataProcessing.getMosaicHeight()
                    * dataProcessing.getMosaicWidth())
                    / ProgressBarsAlgorithms.ONE_HUNDRED_PERCENT);

            for (int row = 0; row < dataProcessing.getMosaicHeight(); row++) {
                xmldocumentString.append(XML_DOCUMENT_ROW_START);

                for (int column = 0; column < dataProcessing
                        .getMosaicWidth(); column++) {
                    if (mosaicMatrix.get(row).get(column).isEmpty()) {
                        xmldocumentString.append(XML_DOCUMENT_ELEMENT_EMPTY);
                    } else {
                        xmldocumentString.append(XML_DOCUMENT_ELEMENT_START);
                        xmldocumentString.append(
                                mosaicMatrix.get(row).get(column).elementAt(0));
                        xmldocumentString.append(XML_DOCUMENT_ELEMENT_CENTER);
                        xmldocumentString.append(
                                mosaicMatrix.get(row).get(column).elementAt(1));
                        xmldocumentString.append(XML_DOCUMENT_ELEMENT_END);
                    }

                    try {
                        SwingUtilities.invokeAndWait(new Runnable() {
                            public void run() {
                                int percent = common.getPercent();
                                percent++;
                                common.setPercent(percent);
                                dataProcessing.refreshProgressBarOutputFiles(
                                        percent, ProgressBarsOutputFiles.XML);
                            }
                        });
                    } catch (final Exception e) {
                    }
                }

                xmldocumentString.append(XML_DOCUMENT_ROW_END);
            }

            xmldocumentString.append(XML_DOCUMENT_END);

            if (!(dataProcessing.generateUTFOutput(xmldocumentString.toString(),
                    "mosaic.xml", true))) {
                error = error + "mosaic.xml " + FileGenerationCommon
                        .getTextbundle().getString("output_outputFiles_38")
                        + ".\n\r";
            }
        }

        return error;
    }
}
