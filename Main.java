
import java.util.Scanner;
import java.util.Random;

class Main {
  static int tempo_maximo = 65535;

  static int n_processos = 3;
  int[] id = new int[n_processos];

  public static void main(String[] args) {

    int[] tempo_execucao = new int[n_processos];
    int[] tempo_chegada = new int[n_processos];
    int[] prioridade = new int[n_processos];
    int[] tempo_espera = new int[n_processos];
    int[] tempo_restante = new int[n_processos];

    Scanner teclado = new Scanner(System.in);

    popular_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);

    imprime_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);

    // Escolher algoritmo
    int alg;

    while (true) {
      System.out.print(
          "\n\nEscolha o argoritmo?: [1=FCFS 2=SJF Preemptivo 3=SJF Não Preemptivo  4=Prioridade Preemptivo 5=Prioridade Não Preemptivo  6=Round_Robin  7=Imprime lista de processos 8=Popular processos novamente 9=Sair]:");
      alg = teclado.nextInt();

      if (alg == 1) { // FCFS
        FCFS(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada);
      } else if (alg == 2) { // SJF PREEMPTIVO
        SJFPremp(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada);
      } else if (alg == 3) { // SJF NÃO PREEMPTIVO
        SJFNoPremp(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada);
      } else if (alg == 4) { // PRIORIDADE PREEMPTIVO
        PRIORIDADEPremp(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
      } else if (alg == 5) { // PRIORIDADE NÃO PREEMPTIVO
          PRIORIDADENoPremp(tempo_execucao, tempo_espera, tempo_restante,
          tempo_chegada, prioridade);

      } else if (alg == 6) { // Round_Robin
        System.out.print("Digite o quantum (tamanho do time slice) para o Round Robin: ");
        int quantum = teclado.nextInt();
        Round_Robin(tempo_execucao, tempo_espera, tempo_restante, quantum);

      } else if (alg == 7) { // IMPRIME CONTEÚDO INICIAL DOS PROCESSOS
        imprime_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
      } else if (alg == 8) { // REATRIBUI VALORES INICIAIS
        popular_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
        imprime_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
      } else if (alg == 9) {
        break;

      }
    }

  }

  public static void popular_processos(int[] tempo_execucao, int[] tempo_espera, int[] tempo_restante,
      int[] tempo_chegada, int[] prioridade) {
    Random random = new Random();
    Scanner teclado = new Scanner(System.in);
    int aleatorio;
    System.out.print("Será aleatório?:  ");
    aleatorio = teclado.nextInt();
    for (int i = 0; i < n_processos; i++) {
      // Popular Processos Aleatorio
      if (aleatorio == 1) {
        tempo_execucao[i] = random.nextInt(10) + 1;
        tempo_chegada[i] = random.nextInt(10) + 1;
        prioridade[i] = random.nextInt(15) + 1;
      }
      // Popular Processos Manual
      else {
        System.out.print("Digite o tempo de execução do processo["+i+"]: ");
        tempo_execucao[i] = teclado.nextInt();
        System.out.print("");
        System.out.print("Digite o tempo de chegada do processo[" + i + "]: ");
        tempo_chegada[i] = teclado.nextInt();
        System.out.print("");
        System.out.print("Digite a prioridade do processo[" + i + "]: ");
        prioridade[i] = teclado.nextInt();
      }
      tempo_restante[i] = tempo_execucao[i];

    }
  }

  public static void imprime_processos(int[] tempo_execucao, int[] tempo_espera, int[] tempo_restante,
      int[] tempo_chegada, int[] prioridade) {
    // Imprime lista de processos
    for (int i = 0; i < n_processos; i++) {
      System.out.println("\nProcesso[" + i + "]: tempo_execucao = " + tempo_execucao[i] + " tempo_restante = " + tempo_restante[i] + " tempo_chegada = " + tempo_chegada[i] + " prioridade = " + prioridade[i]);
    }
  }

  public static void imprime_stats(int[] espera) {
    int[] tempo_espera = espera.clone();
    double tempo_medio = 0.0;
    int total_tempo_espera = 0;

    System.out.println("\nTempo de espera de cada processo:");
    for (int i = 0; i < n_processos; i++) {
      System.out.println("Processo[" + i + "]: tempo_espera = " + tempo_espera[i]);
      total_tempo_espera += tempo_espera[i];
    }

    if (n_processos > 0) {
      tempo_medio = (double) total_tempo_espera / n_processos;
    }

    System.out.printf("Tempo médio de espera: %.2f\n", tempo_medio);
  }

  public static void FCFS(int[] execucao, int[] espera, int[] restante, int[] chegada) {
    int processoAtual = 0;
    int[] tempo_execucao = execucao.clone();
    int[] tempo_espera = espera.clone();
    int[] tempo_restante = restante.clone();
    int[] tempo_chegada = chegada.clone();
    int processo = 0;

    System.out.println(" ");
    for (int i = 1; i < 999999; i++) {
      System.out.println("tempo[" + i + "]: processo[" + processo + "] restante = " + (tempo_restante[processo] - 1));

      if (tempo_restante[processo] == 1) {
        if (processo == n_processos - 1) {
          break;
        } else {
          processo++;
          tempo_espera[processo] = i;
        }
      } else {
        tempo_restante[processo]--;
      }
    }

    imprime_stats(tempo_espera);
  }

  public static void SJFPremp(int[] execucao, int[] espera, int[] restante, int[] chegada) {
    int[] tempo_execucao = execucao.clone();
    int[] tempo_espera = espera.clone();
    int[] tempo_restante = restante.clone();
    int[] tempo_chegada = chegada.clone();
    int tempo = 1;
    int processoAtual = -1;
    int count = 0;
    int menorTempo;

    while (count < n_processos) {
      menorTempo = tempo_maximo;
      for (int processo = 0; processo < n_processos; processo++) {
        if (tempo_restante[processo] > 0 && tempo_chegada[processo] <= tempo && tempo_restante[processo] < menorTempo) {
          menorTempo = tempo_restante[processo];
          processoAtual = processo;
        }
      }

      if (processoAtual == -1) {
        System.out.println("tempo[" + tempo + "]: nenhum processo está pronto");
      } else {
        if (tempo_restante[processoAtual] == tempo_execucao[processoAtual]) {
          tempo_espera[processoAtual] = tempo - tempo_chegada[processoAtual];
        }

        tempo_restante[processoAtual]--;

        System.out
            .println("tempo[" + tempo + "]: processo[" + processoAtual + "] restante=" + tempo_restante[processoAtual]);

        if (tempo_restante[processoAtual] == 0) {
          processoAtual = -1;
          count++;
        }
      }
      tempo++;
    }

    imprime_stats(tempo_espera);
  }

  public static void SJFNoPremp(int[] execucao, int[] espera, int[] restante, int[] chegada) {
    int[] tempo_execucao = execucao.clone();
    int[] tempo_espera = espera.clone();
    int[] tempo_restante = restante.clone();
    int[] tempo_chegada = chegada.clone();

    int processoAtual = -1;
    int count = 0;
    int menorTempo = tempo_maximo;
    int tempo = 1;

    while (tempo < tempo_maximo) {
      if (processoAtual == -1) {
        for (int processo = 0; processo < n_processos; processo++) {
          if ((tempo_restante[processo] != 0) && (tempo_chegada[processo] <= tempo)
              && tempo_restante[processo] < menorTempo) {
            menorTempo = tempo_restante[processo];
            processoAtual = processo;
          }
        }
      }
      if (processoAtual == -1) {
        System.out.println("tempo[" + tempo + "]: nenhum processo está pronto");
      } else {
        if (tempo_restante[processoAtual] == tempo_execucao[processoAtual]) {
          tempo_espera[processoAtual] = tempo - tempo_chegada[processoAtual];
        }
        tempo_restante[processoAtual]--;
        System.out
            .println("tempo[" + tempo + "]: processo[" + processoAtual + "] restante=" + tempo_restante[processoAtual]);
        if (tempo_restante[processoAtual] == 0) {
          processoAtual = -1;
          menorTempo = tempo_maximo;
          count++;
          if (count == n_processos) {
            break;
          }
        }
      }
      tempo++;
    }

    imprime_stats(tempo_espera);
  }

  public static void PRIORIDADEPremp(int[] execucao, int[] espera, int[] restante, int[] chegada, int[] prioridade) {
    int[] tempo_execucao = execucao.clone();
    int[] tempo_espera = espera.clone();
    int[] tempo_restante = restante.clone();
    int[] tempo_chegada = chegada.clone();
    int[] prioridade_temp = prioridade.clone();

    int processoAtual = -1;
    int count = 0;
    int menorPrioridade;
    int tempo = 1;

    while (count < n_processos) {
      menorPrioridade = Integer.MAX_VALUE;
      for (int processo = 0; processo < n_processos; processo++) {
        if ((tempo_restante[processo] != 0) && (tempo_chegada[processo] <= tempo) && (prioridade_temp[processo] < menorPrioridade)) {
          menorPrioridade = prioridade_temp[processo];
          processoAtual = processo;
        }
      }
      if (processoAtual == -1) {
        System.out.println("tempo[" + tempo + "]: nenhum processo está pronto");
      } else {
        if (tempo_restante[processoAtual] == tempo_execucao[processoAtual]) {
          tempo_espera[processoAtual] = tempo - tempo_chegada[processoAtual];
        }
        tempo_restante[processoAtual]--;
        System.out.println("tempo[" + tempo + "]: processo[" + processoAtual + "] restante=" + tempo_restante[processoAtual]);
        if (tempo_restante[processoAtual] == 0) {
          processoAtual = -1;
          count++;
        }
      }
      tempo++;
    }

    imprime_stats(tempo_espera);
  }

  
   public static void PRIORIDADENoPremp(int[] execucao, int[] espera, int[]
   restante, int[] chegada, int[] prioridade) {
   int[] tempo_execucao = execucao.clone();
   int[] tempo_espera = espera.clone();
   int[] tempo_restante = restante.clone();
   int[] tempo_chegada = chegada.clone();
   int[] prioridade_temp = prioridade.clone();
   
   int processoAtual = -1;
    int count = 0;
    int menorPrioridade = Integer.MAX_VALUE;
    int tempo = 1;

    while (tempo < tempo_maximo) {
      if(processoAtual == -1) {
        for (int processo = 0; processo < n_processos; processo++) {
          if ((tempo_restante[processo] != 0) && (tempo_chegada[processo] <= tempo) && prioridade_temp[processo] 
 < menorPrioridade) {
            menorPrioridade = prioridade_temp[processo];
            processoAtual = processo;
          }
        }
      }
      if (processoAtual == -1) {
        System.out.println("tempo[" + tempo + "]: nenhum processo está pronto");
      } else {
        if (tempo_restante[processoAtual] == tempo_execucao[processoAtual]) {
          tempo_espera[processoAtual] = tempo - tempo_chegada[processoAtual];
        }
        tempo_restante[processoAtual]--;
        System.out.println("tempo[" + tempo + "]: processo[" + processoAtual + "] restante=" + tempo_restante[processoAtual]);
        if (tempo_restante[processoAtual] == 0) {
          processoAtual = -1;
          menorPrioridade = Integer.MAX_VALUE;
          count++;
          if (count == n_processos) {
            break;
          }
        }
      }
      tempo++;
    }

    imprime_stats(tempo_espera);
  }

  public static void Round_Robin(int[] execucao, int[] espera, int[] restante, int quantum) {
    int[] tempo_execucao = execucao.clone();
    int[] tempo_espera = espera.clone();
    int[] tempo_restante = restante.clone();
    imprime_stats(tempo_espera);
  }
}