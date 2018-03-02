package com.company;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class Main {

    public static void rateMovieinDB(){
        showTitlesandActors();
        Scanner input = new Scanner(System.in);
        System.out.println("Input title of movie you would like to rate!!! *Exact Spelling*");
        String title = input.nextLine();

        try{
            Connection conn = GetConnection.get();

            PreparedStatement statement = conn.prepareStatement("SELECT id FROM movies WHERE movies.title = ?");
            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Scanner input2 = new Scanner(System.in);
                System.out.println("Rate this movie from 1 which is the worst and 5");
                double rating = input2.nextDouble();

                if (rating <= 5 && rating >= 1){
                PreparedStatement addratings = conn.prepareStatement("INSERT INTO ratings (movie_id, movie_rating) VALUES (?, ?)");
                addratings.setInt(1,resultSet.getInt("id"));
                addratings.setDouble(2,rating);
                addratings.execute();}
                else {
                    System.out.println("Invalid Rating");
                }

        }
        else {
                System.out.println("Could Not Find Movie!!!");
            }
        }catch (SQLException e){

        }
    }

    public static void topThreeRatings(){
        try {
            Connection conn = GetConnection.get();
            PreparedStatement statement = conn.prepareStatement("SELECT m.title, round(AVG(r.movie_rating),2) " +
                    "as avg FROM movies m, ratings r where m.id=r.movie_id group by m.title order by avg desc limit 3");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                System.out.println(resultSet.getString("title")+": "+resultSet.getString("avg")+
                        "\n-----------------------------------");
            }
        }
        catch (SQLException e){
            e.getErrorCode();
        }
    }

    public static void updateMainCharacter(){
        Scanner input = new Scanner(System.in);
        System.out.println("What is the title of movie related to the main character?? *Exact Spelling*");
        String title = input.nextLine();

        try {
            Connection conn = GetConnection.get();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM movies WHERE movies.title = ?");
            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                Scanner input2 = new Scanner(System.in);
                System.out.println("What is the new name??");
                String main_char_name = input2.nextLine();
                PreparedStatement updatestatement = conn.prepareStatement("UPDATE movies SET main_char_name = ? WHERE title = ?");
                updatestatement.setString(1, main_char_name);
                updatestatement.setString(2, title);
                updatestatement.executeQuery();
            }
        }catch (SQLException e){
            if (e.getErrorCode() == 0){
                System.out.println("The Update Was Successful!!!");
            }
            else {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void deleteMovie(){
        while (true) {
            Scanner input = new Scanner(System.in);
            System.out.println("What is the title of the movie??");
            String title = input.nextLine();

            try {
                Connection conn = GetConnection.get();

                PreparedStatement statement = conn.prepareStatement("SELECT id FROM movies WHERE movies.title = ?");
                statement.setString(1, title);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    System.out.println("Movie found in database...");
                    PreparedStatement deleteratings = conn.prepareStatement("DELETE FROM ratings WHERE ? = ratings.movie_id");
                    deleteratings.setInt(1,resultSet.getInt("id"));
                    deleteratings.execute();
                    PreparedStatement deletestatement = conn.prepareStatement(" DELETE FROM movies WHERE movies.title = ?");
                    deletestatement.setString(1, title);
                    deletestatement.executeQuery();
                } else {
                    System.out.println("Movie Not In Database!!");
                }
            } catch (SQLException e) {
                if (e.getErrorCode() == 0) {
                    System.out.println("The Delete Was A Success!!!");
                    System.out.println("Is That All For Now... Yes or No");
                    if (input.next().toUpperCase().equals("YES")) {
                        break;
                    }
                } else {
                    System.out.println(e.getMessage());
                }
            }
        }

    }


    public static void returnRandomMovie(){
        try {
            Random rand = new Random();
            Connection conn = GetConnection.get();
            PreparedStatement statement =conn.prepareStatement("SELECT * FROM movies");
            ArrayList movieNames = new ArrayList<String> ();
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                movieNames.add(resultSet.getString("title"));
            }
            int  n = rand.nextInt(movieNames.size());
            System.out.println(movieNames.get(n));

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public static void showTitlesandActors(){
        try {
            Connection conn = GetConnection.get();
            PreparedStatement statement =conn.prepareStatement("SELECT * FROM movies");
            ResultSet resultSet = statement.executeQuery();
            System.out.println("Title: Main Character\n-----------------------------------");
            while (resultSet.next()){
                System.out.println(resultSet.getString("title")+": "+resultSet.getString("main_char_name")+
                        "\n-----------------------------------");
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public static void addFavoriteMovie(){
        while (true){
        Scanner scanner1 = new Scanner(System.in);
        System.out.println("What is the title of your favorite movie???");
        String title = scanner1.nextLine();

        Scanner scanner2 = new Scanner(System.in);
        System.out.println("Okay what is your name??");
        String student_name = scanner2.nextLine();

        Scanner scanner3 = new Scanner(System.in);
        System.out.println("What is the main character name??");
        String main_char_name = scanner3.nextLine();

        Scanner scanner4 = new Scanner(System.in);
        System.out.println("What would you rate this movie from 1 being the worst and 5 being the best?!?!?!?!");
        double rating = scanner4.nextDouble();
        if (rating > 5.0 || rating < 1.0){
            System.out.println("That is an invalid rating");
            break;
        }

        try {
            Connection conn = GetConnection.get();
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO movies (title, student_name, main_char_name) VALUES (?,?,?)");
            statement.setString(1,title);
            statement.setString(2,student_name);
            statement.setString(3,main_char_name);
            statement.execute();
            PreparedStatement getID = conn.prepareStatement(
                    "SELECT id FROM movies WHERE movies.title = ?"
            );
            getID.setString(1,title);
            ResultSet resultSet = getID.executeQuery();
            resultSet.next();
            PreparedStatement ratingstatement = conn.prepareStatement(
                    "INSERT INTO ratings (movie_id, movie_rating) VALUES (?, ?)");
            ratingstatement.setInt(1,resultSet.getInt("id"));
            ratingstatement.setDouble(2,rating);
            ratingstatement.executeQuery();

        }catch (SQLException e){
//
            if (e.getErrorCode() == 0){
            System.out.println("You and Your movie was added!!!");
                Scanner input = new Scanner(System.in);
                System.out.println("Is That All For Now... Yes or No");
                if (input.next().toUpperCase().equals("YES")){
                    break;
                }
            }
            else {
                System.out.println(e.getMessage());
            }
        }}
    }

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        System.out.println("Would you like To\n\t1) Add New Favorite Movie!!!\n\t" +
                "2) Get Random Movie!!!\n\t" +
                "3) See all movie titles and main character!!!\n\t" +
                "4) Delete Movie!!!\n\t" +
                "5) Update Main Character Name!!\n\t" +
                "6) Get Top 3 Rated Movies in Database!!!\n\t" +
                "7) Add Review!!!");
        String choice = input.next();

        if (choice.equals("1")){
        addFavoriteMovie();
        } else if (choice.equals("2")){
            returnRandomMovie();
        }
        else if (choice.equals("3")){
            showTitlesandActors();
        } else if (choice.equals("4")){
            deleteMovie();
        } else if (choice.equals("5")){
            updateMainCharacter();
        } else if (choice.equals("6")){
            topThreeRatings();
        } else if (choice.equals("7")){
            rateMovieinDB();
        }
        else {
            System.out.println("Error in Input try again!!");
        }
    }
}
