package it.adawant.demo.warehouse.utils;

public interface BaseCommand<T> {

    T execute() throws Exception;
}
