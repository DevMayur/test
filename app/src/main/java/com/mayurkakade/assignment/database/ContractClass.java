package com.mayurkakade.assignment.database;

public class ContractClass {
    public static String DATABASE_NAME = "db_assignment";
    public static int DATABASE_VERSION = 1;

    public static class TitlesContract {
        public static String TABLE_TITLE = "tbl_titles";
        public static String ID = "id";
        public static String TITLE = "title";
    }

    public static class ImagesContract {
        public static String TABLE_TITLE = "tbl_images";
        public static String ID = "id";
        public static String TITLE_ID = "title_id";
        public static String IMAGE_URI = "img_uri";
    }

}
