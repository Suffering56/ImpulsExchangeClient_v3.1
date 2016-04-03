package impulsexchangeclient.options;

public class Options {

    public static String getDepartmentName() {
        return departmentName;
    }

    public static void setDepartmentName(String departmentName) {
        Options.departmentName = departmentName;
    }

    public static String getSwndFilePath() {
        return swndFilePath;
    }

    public static void setSwndFilePath(String swndFilePath) {
        Options.swndFilePath = swndFilePath;
    }

    public static String getSwndFileName() {
        return swndFileName;
    }

    public static void setSwndFileName(String swndFileName) {
        Options.swndFileName = swndFileName;
    }

    public static String getFirebirdDatabasePath() {
        return firebirdDatabasePath;
    }

    public static void setFirebirdDatabasePath(String firebirdDatabasePath) {
        Options.firebirdDatabasePath = firebirdDatabasePath;
    }

    public static String getFirebirdUser() {
        return firebirdUser;
    }

    public static void setFirebirdUser(String firebirdUser) {
        Options.firebirdUser = firebirdUser;
    }

    public static String getFirebirdPassword() {
        return firebirdPassword;
    }

    public static void setFirebirdPassword(String firebirdPassword) {
        Options.firebirdPassword = firebirdPassword;
    }

    public static String getFirebirdEncoding() {
        return firebirdEncoding;
    }

    public static void setFirebirdEncoding(String firebirdEncoding) {
        Options.firebirdEncoding = firebirdEncoding;
    }

    public static String getFbserverPath() {
        return fbserverPath;
    }

    public static void setFbserverPath(String fbserverPath) {
        Options.fbserverPath = fbserverPath;
    }

    public static String getMySqlAddress() {
        return mySqlAddress;
    }

    public static void setMySqlAddress(String mySqlAddress) {
        Options.mySqlAddress = mySqlAddress;
    }

    public static String getMySqlPort() {
        return mySqlPort;
    }

    public static void setMySqlPort(String mySqlPort) {
        Options.mySqlPort = mySqlPort;
    }

    public static String getMySqlDatabaseName() {
        return mySqlDatabaseName;
    }

    public static void setMySqlDatabaseName(String mySqlDatabaseName) {
        Options.mySqlDatabaseName = mySqlDatabaseName;
    }

    public static String getMySqlUser() {
        return mySqlUser;
    }

    public static void setMySqlUser(String mySqlUser) {
        Options.mySqlUser = mySqlUser;
    }

    public static String getMySqlPassword() {
        return mySqlPassword;
    }

    public static void setMySqlPassword(String mySqlPassword) {
        Options.mySqlPassword = mySqlPassword;
    }

    public static String getAdminPassword() {
        return adminPassword;
    }

    public static void setAdminPassword(String adminPassword) {
        Options.adminPassword = adminPassword;
    }

    //настройки отдела
    private static String departmentName;
    private static String swndFilePath;
    private static String swndFileName;
    //настройки Firebird
    private static String firebirdDatabasePath;
    private static String firebirdUser;
    private static String firebirdPassword;
    private static String firebirdEncoding;
    private static String fbserverPath;
    //настройки MySql
    private static String mySqlAddress;
    private static String mySqlPort;
    private static String mySqlDatabaseName;
    private static String mySqlUser;
    private static String mySqlPassword;
    //настройки администратора
    private static String adminPassword;
}
