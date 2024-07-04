package com.banllproject;

import com.banllproject.view.Menu;

public class App {
    public static void main( String[] args ) {
        ConexaoNeo4j.getInstance();
        Menu.criaMenu();
        ConexaoNeo4j.getInstance().close();
        Menu.closeScanner();
        
    }
}
