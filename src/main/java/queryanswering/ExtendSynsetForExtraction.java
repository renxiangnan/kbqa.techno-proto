package queryanswering;

import java.util.HashMap;
import java.util.Map;

public interface ExtendSynsetForExtraction {

    Map<String, String> EXTEND_TYPE_SYNSET = new HashMap<String, String>() {{
        put("NonprofitOrganizations", "dbo:Non-ProfitOrganisation");
        put("GivenNames", "dbo:GivenName");
        put("JamesBondMovies","yago:JamesBondFilms");
        put("TVShows", "dbo:TelevisionShow");
        put("USState", "yago:StatesOfTheUnitedStates");
        put("USStates", "yago:StatesOfTheUnitedStates");
        put("Europe", "yago:EuropeanCountries");
        put("Africa", "yago:AfricanCountries");
    }};

    Map<String, String> EXTEND_ENTITY_SYNSET = new HashMap<String, String>() {{
        put("Prince_Charles", "Charles,_Prince_of_Wales");
    }};


}
