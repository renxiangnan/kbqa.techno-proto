package dictionary.relationmention;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class DependencyTags {
    /**
     *   SUBJ - link between a verb and its subject,
     *          e.g. 'Clinton defeated Dole' - subj(defeated,Clinton),
     *          'what she said is untrue' - subj(is,what she said)
     *
     *   NSUBJ - link between a verb and an NP subject,
     *           e.g. 'Clinton defeated Dole' - nsubj(defeated,Clinton)
     *
     *   NSUBJPASS - link between a passive participle and an NP surface subject,
     *               e.g. 'Dole was defeated by Clinton' - nsubjpass(defeated,Dole)
     *
     *   CSUBJ - link between a verb and a CP subject,
     *           e.g. 'what she said makes sense' - csubj(makes,said),
     *           'what she said is untrue' - ccsubj(untrue,said)
     *
     *   CSUBJPASS - link between a passive participle and a CP surface subject,
     *               e.g. 'that she lied was suspected by everyone' - csubjpass(suspected,lied)
     *
     *   XSUBJ - link between a controlled verb and its controlling subject,
     *           e.g. 'Tom likes to eat fish' - xsubj(eat,Tom)
     *
     *   POSS - link from a noun to a possessive adjective or noun,
     *          e.g. 'their offices' - poss(offices,their), 'Bill's clothes' - poss(clothes,Bill)
     *
     *   PARTMOD - link from a noun or verb to a participle postmodifier,
     *             e.g. 'truffles picked during the spring are tasty' - partmod(truffles,picked),
     *             'Bill picked Fred for the team, demonstrating his incompetence' - partmod(picked,demonstrating)
     *
     */
    final static Set<String> subjectLikeRelation =
            new HashSet<>(Arrays.
                    asList("subj", "nsubj", "nsubjpass", "csubj", "csubjpass", "xsubj", "poss", "partmod"));

    /**
     *   OBJ - link between a verb and one of its objects,
     *         e.g. 'she gave me a raise' - obj(gave,me), obj(gave,raise)
     *
     *   POBJ - link between a preposition and its object, e.g. 'on the chair' - pobj(on,chair)
     *
     *   DOBJ - link between a verb and one of its accusative objects,
     *          e.g. 'she gave me a raise' - dobj(gave,raise)
     *
     *   IOBJ - link between a verb and its dative object,
     *          e.g. 'she gave me a raise' - iobj(gave,me)
     *
     */
    static {
        final Set<String> objectLikeRelation = new HashSet<>(Arrays.
                    asList("obj", "pobj", "dobj", "iobj"));

        final Set<String> originalPrepositions = new HashSet<String>(Arrays.
                    asList("in", "at", "on", "with", "to", "from", "before", "after", "of", "for", "as"));

        final Set<String> complementPrepositions = new HashSet<String>(Arrays.
                    asList("aboard", "about", "above", "across", "after", "against", "again",
                            "along", "alongside", "amid", "among", "around", "as", "astride",
                            "at", "atop", "ontop", "bar", "before", "behind", "below", "beneath",
                            "beside", "besides", "between", "beyond", "but", "by", "circa", "come",
                            "despite", "down", "during", "except", "for", "from", "in", "inside", "into",
                            "less", "like", "minus", "near", "notwithstanding", "of", "off", "on",
                            "onto", "opposite", "out", "outside", "over", "past", "per", "plus",
                            "save", "short", "since", "than", "through", "throughout", "till", "to",
                            "toward", "towards", "under", "underneath", "unlike", "until", "up", "upon",
                            "upside", "versus", "via", "with", "within", "without", "worth"
                    ));
    }
}
