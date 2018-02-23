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
    String REG_TONE = "[cdefgabCDEFGAB]+[b#]*";
    String REG_TEMPO = "-?\\d+";
    String REG_NUMBER = "[123456789][0123456789]*";
    String REG_NAME = "[a-zA-Z]+[a-z-A-Z0-9 ]*";
}
