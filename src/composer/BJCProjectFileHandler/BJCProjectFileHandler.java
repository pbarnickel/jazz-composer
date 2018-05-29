/*
    Description:    Class to read and write BJC-Project-Files
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           19.03.2018
 */

package composer.BJCProjectFileHandler;

import composer.ComposerClasses.Composer;
import composer.Model.*;
import composer.Settings.Settings;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.sound.midi.MidiUnavailableException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class BJCProjectFileHandler {

    private Settings settings = new Settings();

    public BJCProjectFileHandler(){
        settings.loadSettings();
    }

    public void writeBJC(Composer composer, String path){
        JSONObject jsonObjectRoot = new JSONObject();

        //General
        JSONObject jsonObjectGeneral = new JSONObject();
        jsonObjectGeneral.put("tempo", composer.getGeneral().getTempo());
        JSONObject jsonObjectGeneralTone = new JSONObject();
        jsonObjectGeneralTone.put("name", composer.getGeneral().getTone().getName());
        jsonObjectGeneralTone.put("pitch", composer.getGeneral().getTone().getPitch());
        jsonObjectGeneral.put("tone", jsonObjectGeneralTone);
        jsonObjectGeneral.put("repeat", composer.getGeneral().getRepeat());
        jsonObjectGeneral.put("humanizerTolerance", composer.getGeneral().getHumanizerTolerance());
        jsonObjectGeneral.put("dynamics", composer.getGeneral().getDynamics());
        jsonObjectRoot.put("general", jsonObjectGeneral);

        //Chordprogression
        JSONArray jsonArrayChordprogression = new JSONArray();
        int length = composer.getChordprogression().getSize();
        for(int i=0; i<length; i++){
            JSONObject jsonObjectPatternelement = new JSONObject();
            jsonObjectPatternelement.put("order", composer.getChordprogression().getPatternelement(i).getOrder());
            jsonObjectPatternelement.put("transpose", composer.getChordprogression().getPatternelement(i).getTranspose());
            jsonObjectPatternelement.put("mode", composer.getChordprogression().getPatternelement(i).getMode());
            jsonObjectPatternelement.put("chordgroup", composer.getChordprogression().getPatternelement(i).getChordgroup().getName());
            jsonObjectPatternelement.put("chord", composer.getChordprogression().getPatternelement(i).getChordName());
            jsonObjectPatternelement.put("chordcomplexity", composer.getChordprogression().getPatternelement(i).getChordcomplexityName());
            jsonObjectPatternelement.put("usage", composer.getChordprogression().getPatternelement(i).getUsage());
            jsonObjectPatternelement.put("tactProportion", composer.getChordprogression().getPatternelement(i).getTactProportion());
            jsonArrayChordprogression.add(jsonObjectPatternelement);
        }
        jsonObjectRoot.put("chordprogression", jsonArrayChordprogression);

        //Backingtrack
        JSONObject jsonObjectBackingtrack = new JSONObject();
        jsonObjectBackingtrack.put("piano", composer.getBackingtrack().getPiano());
        jsonObjectBackingtrack.put("bass", composer.getBackingtrack().getBass());
        jsonObjectBackingtrack.put("drums", composer.getBackingtrack().getDrums());
        jsonObjectBackingtrack.put("deviation", composer.getBackingtrack().getDeviation().getRowData());
        jsonObjectBackingtrack.put("walkingBass", composer.getBackingtrack().getWalkingBass());
        jsonObjectRoot.put("backingtrack", jsonObjectBackingtrack);

        //Melody
        JSONObject jsonObjectMelody = new JSONObject();
        jsonObjectMelody.put("trumpet", composer.getMelody().getState());
        jsonObjectMelody.put("melodyByScale", composer.getMelody().getMelodyByScale());
        jsonObjectMelody.put("sortOfPitches", composer.getMelody().getSortOfPitches());
        jsonObjectMelody.put("bebop", composer.getMelody().getBebop());
        if(composer.getMelody().getMajorScale() != null && composer.getMelody().getMinorScale() != null) {
            JSONObject jsonObjectMelodyMajorScale = new JSONObject();
            jsonObjectMelodyMajorScale.put("scalegroup", composer.getMelody().getMajorScale().getGroup());
            jsonObjectMelodyMajorScale.put("scale", composer.getMelody().getMajorScale().getName());
            jsonObjectMelody.put("majorScale", jsonObjectMelodyMajorScale);
            JSONObject jsonObjectMelodyMinorScale = new JSONObject();
            jsonObjectMelodyMinorScale.put("scalegroup", composer.getMelody().getMinorScale().getGroup());
            jsonObjectMelodyMinorScale.put("scale", composer.getMelody().getMinorScale().getName());
            jsonObjectMelody.put("minorScale", jsonObjectMelodyMinorScale);
        }
        jsonObjectRoot.put("melody", jsonObjectMelody);

        //Swing
        JSONArray jsonArraySwing = new JSONArray();
        length = composer.getSwing().getSize();
        for(int i=0; i<length; i++){
            JSONObject jsonObjectSwingEighth = new JSONObject();
            jsonObjectSwingEighth.put("position", composer.getSwing().getEighth(i).getPosition());
            jsonObjectSwingEighth.put("rowData", composer.getSwing().getEighth(i).getRowData());
            JSONObject jsonObjectSwingEighthRange = new JSONObject();
            jsonObjectSwingEighthRange.put("start", composer.getSwing().getEighth(i).getRange().getStart());
            jsonObjectSwingEighthRange.put("end", composer.getSwing().getEighth(i).getRange().getEnd());
            jsonObjectSwingEighth.put("range", jsonObjectSwingEighthRange);
            jsonArraySwing.add(jsonObjectSwingEighth);
        }
        jsonObjectRoot.put("swing", jsonArraySwing);

        //write Settings
        try (FileWriter writer = new FileWriter(path)){
            writer.write(jsonObjectRoot.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Composer readBJC(String path) throws MidiUnavailableException {

        Composer composer = null;
        JSONParser parser = new JSONParser();

        try {

            //read file
            FileReader reader = new FileReader(path);
            Object obj = parser.parse(reader);
            JSONObject jsonObjectRoot = (JSONObject) obj;

            //General
            JSONObject jsonObjectGeneral = (JSONObject) jsonObjectRoot.get("general");
            int tempo = Integer.parseInt(jsonObjectGeneral.get("tempo").toString());
            int repeat = Integer.parseInt(jsonObjectGeneral.get("repeat").toString());
            double humanizerTolerance = Double.parseDouble(jsonObjectGeneral.get("humanizerTolerance").toString());
            double dynamics = Double.parseDouble(jsonObjectGeneral.get("dynamics").toString());
            JSONObject jsonObjectGeneralTone = (JSONObject) jsonObjectGeneral.get("tone");
            String toneName = (String) jsonObjectGeneralTone.get("name");
            int tonePitch = Integer.parseInt(jsonObjectGeneralTone.get("pitch").toString());
            Tone tone = new Tone(toneName, tonePitch);
            General general = new General(tempo, repeat, humanizerTolerance, dynamics, tone);

            //Chordprogression
            JSONArray jsonArrayChordprogression = (JSONArray) jsonObjectRoot.get("chordprogression");
            int length = jsonArrayChordprogression.size();
            ArrayList<Patternelement> patternelements = new ArrayList<Patternelement>();
            for(int i=0; i<length; i++){
                JSONObject jsonObjectPatternelement = (JSONObject) jsonArrayChordprogression.get(i);
                int order = Integer.parseInt(jsonObjectPatternelement.get("order").toString());
                int transpose = Integer.parseInt(jsonObjectPatternelement.get("transpose").toString());
                int indexChordgroup = settings.getIndexOfMusicElement(settings.getChordgroups(), (String) jsonObjectPatternelement.get("chordgroup"));
                MusicStructureGroup chordgroup = settings.getChordgroups().get(indexChordgroup);
                int indexChord = settings.getIndexOfMusicElement(settings.getChordgroups().get(indexChordgroup).getMusicStructures(), (String) jsonObjectPatternelement.get("chord"));
                MusicStructure chord = settings.getChordgroups().get(indexChordgroup).getMusicStructures().get(indexChord);
                int indexChordcomplexity = settings.getIndexOfMusicElement(settings.getChordcomplexities(), (String) jsonObjectPatternelement.get("chordcomplexity"));
                Chordcomplexity chordcomplexity = settings.getChordcomplexities().get(indexChordcomplexity);
                String tactProportion = (String) jsonObjectPatternelement.get("tactProportion");
                Patternelement patternelement = new Patternelement(order, transpose, chordgroup, chord, chordcomplexity, tactProportion);
                patternelements.add(patternelement);
            }
            Chordprogression chordprogression = new Chordprogression(patternelements);

            //Backingtrack
            JSONObject jsonObjectBackingtrack = (JSONObject) jsonObjectRoot.get("backingtrack");
            boolean piano = Boolean.parseBoolean(jsonObjectBackingtrack.get("piano").toString());
            boolean bass = Boolean.parseBoolean(jsonObjectBackingtrack.get("bass").toString());
            boolean drums = Boolean.parseBoolean(jsonObjectBackingtrack.get("drums").toString());
            Deviation deviation = new Deviation(Double.parseDouble(jsonObjectBackingtrack.get("deviation").toString()));
            double walkingBass = Double.parseDouble(jsonObjectBackingtrack.get("walkingBass").toString());
            Backingtrack backingtrack = new Backingtrack(piano, bass, drums, deviation, walkingBass);

            //Melody
            JSONObject jsonObjectMelody = (JSONObject) jsonObjectRoot.get("melody");
            boolean trumpet = Boolean.parseBoolean(jsonObjectMelody.get("trumpet").toString());
            boolean melodyByScale = Boolean.parseBoolean(jsonObjectMelody.get("melodyByScale").toString());
            double sortOfPitches = Double.parseDouble(jsonObjectMelody.get("sortOfPitches").toString());
            double bebop = Double.parseDouble(jsonObjectMelody.get("bebop").toString());
            MusicStructure majorScale = null;
            MusicStructure minorScale = null;
            if(jsonObjectMelody.get("majorScale") != null && jsonObjectMelody.get("minorScale") != null) {
                JSONObject jsonObjectMelodyMajorScale = (JSONObject) jsonObjectMelody.get("majorScale");
                int indexScalegroup = settings.getIndexOfMusicElement(settings.getScalegroups(), jsonObjectMelodyMajorScale.get("scalegroup").toString());
                int indexScale = settings.getIndexOfMusicElement(settings.getScalegroups().get(indexScalegroup).getMusicStructures(), jsonObjectMelodyMajorScale.get("scale").toString());
                majorScale = settings.getScalegroups().get(indexScalegroup).getMusicStructures().get(indexScale);
                JSONObject jsonObjectMelodyMinorScale = (JSONObject) jsonObjectMelody.get("minorScale");
                indexScalegroup = settings.getIndexOfMusicElement(settings.getScalegroups(), jsonObjectMelodyMinorScale.get("scalegroup").toString());
                indexScale = settings.getIndexOfMusicElement(settings.getScalegroups().get(indexScalegroup).getMusicStructures(), jsonObjectMelodyMinorScale.get("scale").toString());
                minorScale = settings.getScalegroups().get(indexScalegroup).getMusicStructures().get(indexScale);
            }
            Melody melody = new Melody(trumpet, melodyByScale, sortOfPitches, bebop, majorScale, minorScale);

            //Swing
            JSONArray jsonArraySwing = (JSONArray) jsonObjectRoot.get("swing");
            length = jsonArraySwing.size();
            ArrayList<Eighth> eighths = new ArrayList<Eighth>();
            for(int i=0; i<length; i++){
                JSONObject jsonObjectSwingEighth = (JSONObject) jsonArraySwing.get(i);
                int position = Integer.parseInt(jsonObjectSwingEighth.get("position").toString());
                double rowData = Double.parseDouble(jsonObjectSwingEighth.get("rowData").toString());
                JSONObject jsonObjectSwingEighthRange = (JSONObject) jsonObjectSwingEighth.get("range");
                int start = Integer.parseInt(jsonObjectSwingEighthRange.get("start").toString());
                int end = Integer.parseInt(jsonObjectSwingEighthRange.get("end").toString());
                Range range = new Range(start, end);
                Eighth eighth = new Eighth(position, rowData, range);
                eighths.add(eighth);
            }
            Swing swing = new Swing(eighths);

            composer = new Composer(general, chordprogression, backingtrack, melody, swing);

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return composer;
    }
}
