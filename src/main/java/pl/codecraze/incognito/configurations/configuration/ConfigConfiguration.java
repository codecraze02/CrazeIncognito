package pl.codecraze.incognito.configurations.configuration;

/**
 * @author CodeCraze (ComplexHub.pl) on 16.06.2025
 **/
public class ConfigConfiguration {

    public Messages messages = new Messages();

    public static class Messages {
        public  String CONFIG_RELOADED = "&aPomyślnie przeładowano konfigurację.";
        public  String INCOGNITO_ENABLED = "&aTryb incognito został &2włączony&a.";
        public  String INCOGNITO_DISABLED = "&cTryb incognito został &4wyłączony&c.";
        public  String INCOGNITO_REFRESHED = "&aTwoje dane incognito zostały &2odświeżone&a.";
        public  String INCOGNITO_ALREADY_ENABLED = "&eTryb incognito jest już &2włączony&e.";
        public  String INCOGNITO_ALREADY_DISABLED = "&eTryb incognito jest już &4wyłączony&e.";
        public  String INCOGNITO_STATUS_ENABLED = "&aTryb incognito: &2WŁĄCZONY &7(Nick: &f%nickname%&7)";
        public  String INCOGNITO_STATUS_DISABLED = "&aTryb incognito: &cWYŁĄCZONY &7(Nick: &f%nickname%&7)";
        public  String PLAYER_NOT_FOUND = "&cNie znaleziono gracza o podanym nicku.";
    }
}
