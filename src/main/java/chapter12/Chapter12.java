package chapter12;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.Result;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import static common.Result.error;
import static common.Result.ok;

class Chapter12 {
  private Chapter12() {
  }

  private static int writeToFile(final String fileName, final String data) throws IOException {
    Files.writeString(Path.of(fileName), data);
    final var data1 = Files.readString(Path.of(fileName));
    return data1.length();
  }

  private static int writeToFileAsync(final String fileName, final String data) throws ExecutionException,
      InterruptedException {
    try (final var executor = Executors.newVirtualThreadPerTaskExecutor()) {
      final var future = executor.submit(() -> {
        Files.writeString(Path.of(fileName), data);
        final var data1 = Files.readString(Path.of(fileName));
        return data1.length();
      });
      return future.get();
    }
  }

  private static int writeToFileAsyncMix(final String fileName, final String data) throws InterruptedException,
      ExecutionException {
    System.out.println("1. This is happening synchronously!");
    Thread.sleep(1000);

    System.out.println("2. Kicking off the background work!");
    try (final var executor = Executors.newVirtualThreadPerTaskExecutor()) {
      final var future = executor.submit(() -> {
        Files.writeString(Path.of(fileName), data);
        Thread.sleep(1000);
        System.out.println("4. This is happening asynchronously!");
        final var data1 = Files.readString(Path.of(fileName));
        return data1.length();
      });

      System.out.println("3. Doing something more, now let's return the result");
      return future.get();
    }
  }

  record Customer(String name, int balance) {
  }

  static <T> Customer loadCustomerFromDb(final T customerId) {
    return new Customer("Isaac", 0);
  }

  static <T> Result<Customer, String> tryGetCustomer(final T customerId) {
    final var customer = loadCustomerFromDb(customerId);
    if (customer.balance() <= 0) {
      return error("Customer is in debt!");
    } else {
      return ok(customer);
    }
  }

  record Request(int customerId) {
  }

  record Response(String customerName) {
  }

  static Response handleRequest(final String json) throws Exception {
    final var objectMapper = new ObjectMapper();
    final var request = objectMapper.readValue(json, Request.class);
    final var customer = tryGetCustomer(request.customerId);
    return switch (customer) {
      case Result.Ok(var c) -> new Response(c.name().toUpperCase());
      case Result.Error(var e) -> throw new Exception("Bad request: " + e);
    };
  }

  public static void main(String[] args) throws Exception {
    Files.writeString(Path.of("foo.txt"), "Hello, world");
    final var text = Files.readString(Path.of("foo.txt"));
    System.out.println(text);

    try (final var executor = Executors.newVirtualThreadPerTaskExecutor()) {
      final var textFuture = executor.submit(() -> Files.readString(Path.of("foo.txt")));
      final var theText = textFuture.get();
      System.out.println(theText);
    }

    final var total = writeToFile("sample.txt", "foo");
    System.out.println(total);

    try (final var executor = Executors.newVirtualThreadPerTaskExecutor()) {
      final var file1 = executor.submit(() -> Files.readString(Path.of("file1.txt"))).get();
      final var file2 = executor.submit(() -> Files.readString(Path.of("file2.txt"))).get();
      final var file3 = executor.submit(() -> Files.readString(Path.of("file3.txt"))).get();
      final var filesComposed = "%s %s %s".formatted(file1, file2, file3);
      System.out.println(filesComposed);
    }

    try (final var executor = Executors.newVirtualThreadPerTaskExecutor()) {
      final var file1Future = executor.submit(() -> Files.readString(Path.of("file1.txt")));
      final var file2Future = executor.submit(() -> Files.readString(Path.of("file2.txt")));
      final var file3Future = executor.submit(() -> Files.readString(Path.of("file3.txt")));
      final var filesParallel = "%s %s %s".formatted(file1Future.get(), file2Future.get(), file3Future.get());
      System.out.println(filesParallel);
    }

    final var theResult = writeToFileAsyncMix("sample.txt", "foo");
    System.out.println(theResult);

    final var response = handleRequest("{\"customerId\": 1}");
  }
}
