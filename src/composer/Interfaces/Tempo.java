/*
        Description:    Interface with constants for tempo.
        Author:         Philipp Barnickel
        Version:        1.0
        Date:           02.02.2018
*/

package composer.Interfaces;

public interface Tempo {
    double LARGO = 44.0D;                       //Breit, ruhig
    double LARGHETTO = 60.0D;                   //Etwas fließender als LARGO
    double ADIAGO = 66.0D;                      //Gemächlich, ruhig, andächtig
    double ANDANTE = 76.0D;                     //Gehend, mäßig bewegt
    double MODERATO = 108.0D;                   //Mäßig bewegt
    double ALLEGRO = 130.0D;                    //Lustig, heiter, munter, schnell
    double PRESTO = 168.0D;                     //Schnell, sehr schnell, rasch, eilig

    double SLOW = 48.0D;
    double MEDIUM_SLOW = 60.0D;
    double MEDIUM = 90.0D;
    double MEDIUM_FAST = 140.0D;
    double FAST = 180.0D;
    double UP_TEMPO = 240.0D;
}