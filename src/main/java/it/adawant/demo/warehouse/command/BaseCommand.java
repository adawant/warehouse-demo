package it.adawant.demo.warehouse.command;

public interface BaseCommand<T> {

    public T execute();
}
