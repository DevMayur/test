package com.mayurkakade.assignment.database;

public class Queries {
    public static String CREATE_TABLE_TITLES = "CREATE TABLE  IF NOT EXISTS " + ContractClass.TitlesContract.TABLE_TITLE +
            " ( " + ContractClass.TitlesContract.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            ContractClass.TitlesContract.TITLE + " text )";

    public static String CREATE_TABLE_IMAGES = "CREATE TABLE  IF NOT EXISTS " + ContractClass.ImagesContract.TABLE_TITLE +
            " ( " + ContractClass.ImagesContract.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            ContractClass.ImagesContract.TITLE_ID + " text ," +
            ContractClass.ImagesContract.IMAGE_URI + " text )";

    public static final String DROP_TABLE_TITLES = "drop table if exists " + ContractClass.TitlesContract.TABLE_TITLE;
    public static final String DROP_TABLE_IMAGES = "drop table if exists " + ContractClass.ImagesContract.TABLE_TITLE;
}
