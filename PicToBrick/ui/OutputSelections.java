package pictobrick.ui;

/**
 * Record containing output file selections. Initial coding by John Watne
 * 10/2023.
 *
 * @param graphic
 * @param configuration
 * @param material
 * @param instruction
 * @param xml
 */
public record OutputSelections(boolean graphic, boolean configuration,
        boolean material, boolean instruction, boolean xml) {

}
