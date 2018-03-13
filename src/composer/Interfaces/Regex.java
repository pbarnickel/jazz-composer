/*
    Description:    Regular-Expressions for user input validation.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           22.02.2018
*/

package composer.Interfaces;

public interface Regex {
    String REG_CHORD_NAME = "[a-zA-Z0-9#]+[a-zA-Z0-9_# ]*";
    String REG_CHORD_USAGE = "[0-9]+(-[0-9]+)*";
    String REG_TONE = "[cdefgabCDEFGAB]{1}";
    String REG_TONE_EXTENDED = "[abcdefgabhCDEFGABH]{1}[#b]?";
    String REG_TEMPO = "-?\\d+";
    String REG_NUMBER = "[1-9][0-9]*";
    String REG_NAME = "[a-zA-Z]+[a-z-A-Z0-9 ]*";
    String REG_TRANSPOSE = "[-]*[0-9]+";
    String REG_TACT_PROPORTION = "Full|Semi";
    String REG_PITCH = "[0-9]+([.][0-9]+)?";
}
