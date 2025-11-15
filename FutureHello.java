/* Disciplina: Programacao Concorrente */
/* Prof.: Silvana Rossetto */
/* Laboratório: 11 */
/* Codigo: Exemplo de uso de futures para checagem de primos */
/* Nome: Yuri Rocha de Albuquerque */
/* DRE: 123166143 */
/* -------------------------------------------------------------------*/

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import java.util.ArrayList;
import java.util.List;


// Classe Callable modificada para retornar 0 se o número não for primo e 1 se o número for primo
class MyCallable implements Callable<Long> {
  private long n;

  // Número a ser testado
  MyCallable(long n) {
    this.n = n;
  }

  //método para execução
  public Long call() throws Exception {
    if(this.n <= 1) {
      return 0l;
    }
    if(this.n == 2) {
      return 1l;
    }
    if(this.n % 2 == 0) {
      return 0l;
    }
    for(int i = 3; i < Math.sqrt(this.n) + 1; i += 2) {
      if(n % i == 0) {
        return 0l;
      }
    }
    return 1l;
  }
}

//classe do método main
public class FutureHello  {
  private static final int N = 10000;
  private static final int NTHREADS = 10;

  public static void main(String[] args) {
    //cria um pool de threads (NTHREADS)
    ExecutorService executor = Executors.newFixedThreadPool(NTHREADS);
    //cria uma lista para armazenar referencias de chamadas assincronas
    List<Future<Long>> list = new ArrayList<Future<Long>>();

    for (long i = 1; i < N; i++) {
      // Aqui, foi feita uma modificação no construtor para passar como parâmetro o número a ser testado pela thread
      Callable<Long> worker = new MyCallable(i);
      /*submit() permite enviar tarefas Callable ou Runnable e obter um objeto Future para acompanhar o progresso e recuperar o resultado da tarefa
       */
      Future<Long> submit = executor.submit(worker);
      list.add(submit);
    }
    //pode fazer outras tarefas...

    //recupera os resultados e faz o somatório final
    long sum = 0;
    for (Future<Long> future : list) {
      try {
        sum += future.get(); //bloqueia se a computação nao tiver terminado
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (ExecutionException e) {
        e.printStackTrace();
      }
    }
    System.out.println("Quantidade de primos entre 1 e N: " + sum);
    executor.shutdown();
  }
}
