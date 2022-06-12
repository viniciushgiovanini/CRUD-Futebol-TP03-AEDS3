
//Trabalho Prático 3 = Algoritmo e Estrutura de Dados 3
//Professor: Felipe
//Aluno: Vinícius Henrique Giovanini
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class compressao {

  // --------------------X-Métodos-de-ajuda-Compressao-X--------------------//

  // --------------------------------------
  // Esses métodos de ajuda são métodos de apoio menos importantes, como para
  // deletar os dados de um arquivo, pegar a qtd de elementos de um vetor,
  // verificar se o vetor esta vazio fazer conversoes entre outros.
  // --------------------------------------

  private void deletaTudo(String caminho) {

    try {
      PrintWriter writer = new PrintWriter(caminho);
      writer.print("");
      writer.close();
    } catch (Exception e) {
      System.out.println("Erro no método deletaTudoCompressao: " + e.getMessage());
    }

  }

  private int qtdElementosString(String[] a) {

    int contador = 0;

    for (int i = 0; i < a.length; i++) {

      if (a[i] != null) {
        contador++;
      } else {
        i = a.length;
      }

    }

    return contador;
  }

  private int qtdElementosInt(int[] a) {
    int contador = 0;
    for (int i = 0; i < a.length; i++) {

      if (a[i] != -1) {
        contador++;
      }

    }
    return contador;
  }

  private int pesquisarNoDicionario(String letraS, String[] dicionarioAtualizando) {

    int posiLetra = -1;

    for (int j = 0; j < dicionarioAtualizando.length; j++) {

      if (letraS.equals(dicionarioAtualizando[j])) {

        posiLetra = j;

      } else if (dicionarioAtualizando[j] == null) {
        j = dicionarioAtualizando.length;
      }

    }

    return posiLetra;

  }

  private int[] inicializarVetor(int[] s) {

    for (int i = 0; i < s.length; i++) {

      s[i] = -1;

    }

    return s;
  }

  private boolean vetorEstaVazio(int[] s) {

    boolean teste = false;

    if (s[0] == -1) {
      teste = true;
    }
    return teste;
  }

  private byte[] deIntParaByteArray(int[] s) {

    byte[] a = new byte[s.length];

    for (int i = 0; i < s.length; i++) {

      a[i] = (byte) s[i];

    }
    return a;
  }

  // --------------------X-Fim-Métodos-de-ajuda-Compressao-X--------------------//

  // --------------------X-Inicio-Métodos-Principais-Compressao-X--------------------//
  // --------------------------------------
  // O método realizarCompressao utiliza a tecnica de compressao pelo LZW com o
  // dicionario retornando um array de byte pois geralmente o valor é mt alto nao
  // cabendo em um int
  // --------------------------------------
  private int[] realizarCompressao(String entrada) {

    String[] dicionario = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
        "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "Ç",
        "Ã", "Á", "Â",
        "É", "Ê", "Í", "Î", "Ó", "Õ", "Ô", "Ú", "Û", " ", "/", ".", "-" };

    String[] compressaoDicionario = new String[dicionario.length * 3];
    System.arraycopy(dicionario, 0, compressaoDicionario, 0, dicionario.length);
    int[] valorCompressao = new int[500];
    valorCompressao = inicializarVetor(valorCompressao);
    int contadorValorCompressao = 0;
    String letraS = "";
    String proxLetra = "";
    String letraParaSalvarnoDic = "";

    int saveValorRetorno = 0;
    int i = 0;
    boolean podeEncerrar = false;

    while (i < entrada.length() && !podeEncerrar) {

      letraS = "";
      char letra = ' ';
      int valorRetorno = 0;

      letra = entrada.charAt(i);
      letraS += letra;
      valorRetorno = pesquisarNoDicionario(letraS, compressaoDicionario);

      if (valorRetorno != -1) {

        proxLetra += letraS;
        boolean podePesquisar = true;
        char letraProx = ' ';

        while (valorRetorno != -1 && !podeEncerrar) {

          saveValorRetorno = valorRetorno;
          if (i < entrada.length()) {
            if (i == entrada.length() - 1) {

              proxLetra = letraS;
              podePesquisar = false;
              podeEncerrar = true;
              valorRetorno = saveValorRetorno;

            } else {

              letraProx = entrada.charAt(i + 1);
              proxLetra += letraProx;

            }
          }

          if (podePesquisar) {
            valorRetorno = pesquisarNoDicionario(proxLetra, compressaoDicionario);
          }

          if (valorRetorno != -1) {

            letraS = proxLetra;
            i++;

          } else {

            letraParaSalvarnoDic = proxLetra;
            i++;
          }

        }

        if (saveValorRetorno != -1) {

          valorCompressao[contadorValorCompressao] = saveValorRetorno;
          contadorValorCompressao++;

        }

        int posicaoNoDicionario = qtdElementosString(compressaoDicionario);
        if (!(letraParaSalvarnoDic.equals(""))) {
          compressaoDicionario[posicaoNoDicionario] = letraParaSalvarnoDic;
        }

        proxLetra = "";
        letraS = "";
        letraParaSalvarnoDic = "";
      } else {
        podeEncerrar = true;
        valorCompressao = inicializarVetor(valorCompressao);
      }

    }

    int[] vRetorno = { -1 };

    if (!vetorEstaVazio(valorCompressao)) {

      int qtdElementos = qtdElementosInt(valorCompressao);
      vRetorno = new int[qtdElementos];

      System.arraycopy(valorCompressao, 0, vRetorno, 0, qtdElementos);

    }

    return vRetorno;
  }

  // --------------------------------------
  // O método toByteArray da compressao pega os elementos e manda para um array de
  // byte, verificando se deve criar um array de byte com os campos compressados
  // caso seja possivel e caso nao seja ele manda o proprio elemento do campo sem
  // compressar no array de byte para nao comprometer o registro.
  // --------------------------------------

  private byte[] toByteArray(short iD, String lapide, String nome, String cnpj,
      String cidade, byte pj, byte pts, int[] nomeArray, int[] cnpjArray, int[] cidadeArray) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);

    // como ta escrevendo a compressao tamanhodoarquivodedados+id,lapide,boolean se
    // for true o nome é um int, se for false é string, nome, boolean verificador
    // pra cnpj, cnpj, boolean verificador cidade, cidade, pj e pontos

    // tamanhoArq+id+lapide+boolean(SETIVERBA)+byte-10(correspondenteaofinaldoarray),
    // tamanhoArq+id+lapide+boolean+nome(SenaoTiverBA),

    fut ft = new fut();

    byte[] ba;
    boolean saveNomeArray = false;
    boolean saveCnpjArray = false;
    boolean saveCidadeArray = false;

    if (!vetorEstaVazio(nomeArray)) {
      saveNomeArray = true;
    }
    boolean podeSetarCnpj = ft.verificaCPNJ(cnpj);

    if (!vetorEstaVazio(cnpjArray) && podeSetarCnpj) {
      saveCnpjArray = true;
    }

    if (!vetorEstaVazio(cidadeArray)) {
      saveCidadeArray = true;
    }

    dos.writeShort(iD);
    dos.writeUTF(lapide);
    if (saveNomeArray) {
      ba = deIntParaByteArray(nomeArray);
      dos.writeBoolean(saveNomeArray);
      dos.write(ba);
      dos.writeByte(-10);
    } else {
      dos.writeBoolean(saveNomeArray);
      dos.writeUTF(nome);
    }

    if (saveCnpjArray) {

      ba = deIntParaByteArray(cnpjArray);
      dos.writeBoolean(saveCnpjArray);
      dos.write(ba);
      dos.writeByte(-10);
    } else {
      dos.writeBoolean(saveCnpjArray);
      dos.writeUTF("");
    }

    if (saveCidadeArray) {

      ba = deIntParaByteArray(cidadeArray);
      dos.writeBoolean(saveCidadeArray);
      dos.write(ba);
      dos.writeByte(-10);
    } else {
      dos.writeBoolean(saveCidadeArray);
      dos.writeUTF(cidade);
    }

    dos.writeByte(pj);
    dos.writeByte(pts);
    return baos.toByteArray();
  }

  // --------------------------------------
  // O método porcentagemDeCompressao pega o tamanho do arquivo principal e do
  // novo gerado e calcula a porcentagem de compactacao, geralmente ela é pequena
  // pois estao sendo comprimidos pequenos valores.
  // --------------------------------------
  private int porcentagemDeCompressao(long depoisCompre, long antesCompre) {

    float a = depoisCompre;
    float b = antesCompre;

    a *= 4;
    b *= 4;

    float resp = a / b;

    resp *= 100;
    int respO = (int) resp;
    respO = 100 - respO;

    if (respO <= 0) {
      respO = -1;
    }

    return respO;
  }

  // --------------------------------------
  // O método compressão vai lendo registro por registro e verificando se deve ser
  // feita a compressao do nome, cnpj e cidade, e mandando para o método
  // realizarCompressao fazendo um novo byte array e salvando no arquivo de
  // compressao presente na pasta compressao.
  //
  // O compressao tbm pega o arquivo de indice principal e a lista invertida e
  // cria um arquivo na pasta compressao salvando esses registros, e salva o
  // arquivo futebol que é o principal compressado, quando é chamado a
  // descompressao ele pega o aquido de indice e a lista da compressao e carrega
  // no principal.
  // --------------------------------------

  public void compressaoLzw(int entrada) {

    // tem que cromprimir nome, cnpj, cidade
    // ARRAYDEBYTE(ID+LAPIDE+NOME+CNPJ+CIDADE+PARTIDASJOGADAS+PONTOS)modelo de cada
    // registro no arquivo principal.

    // variaveis do método realizar compressao

    RandomAccessFile arqPrinci;
    RandomAccessFile arqCompri;
    RandomAccessFile arqLista;
    RandomAccessFile arqIndice;
    try {
      String caminhodoArqCompri = "src/database/compressão/futebolCompressao" + entrada + ".db";
      String caminhodoArqLista = "src/database/compressão/ListaCompressao" + entrada + ".db";
      String caminhoArqIndice = "src/database/compressão/IndiceCompressao" + entrada + ".db";
      arqPrinci = new RandomAccessFile("src/database/futebol.db", "r");
      arqCompri = new RandomAccessFile(caminhodoArqCompri, "rw");
      arqLista = new RandomAccessFile(caminhodoArqLista, "rw");
      arqIndice = new RandomAccessFile(caminhoArqIndice, "rw");
      deletaTudo(caminhodoArqCompri);
      deletaTudo(caminhodoArqLista);
      deletaTudo(caminhoArqIndice);
      Short iDclube = -1;
      String lapide = "";
      String ftNome = "";
      String ftCnpj = "";
      String ftCidade = "";
      int[] receberByteComprimidoNome = { -1 };
      int[] receberByteComprimidoCnpj = { -1 };
      int[] receberByteComprimidoCidade = { -1 };

      byte[] pegarLista = Files.readAllBytes(Paths.get("src/database/listainvertida.db"));
      byte[] pegarIndice = Files.readAllBytes(Paths.get("src/database/aindices.db"));

      arqLista.write(pegarLista);
      arqIndice.write(pegarIndice);

      int totalDeRegistros = (int) arqIndice.length() / 13;

      if (arqPrinci.length() != 0) {
        int contador = 0;
        short idInicio = arqPrinci.readShort();
        arqCompri.writeShort(idInicio);
        byte b[] = { -7 };

        while ((contador < totalDeRegistros) && arqPrinci.getFilePointer() < arqPrinci.length()) {

          // Os sets tem que salvar em byte e nao em string entao nao poe usar a classa
          // indice, pois se não vai salvar com tipo primitivo, tem que fazer um novo
          // tobyteArray fazer dia 10/06

          int tamanhodoRegistro = arqPrinci.readInt();// apagar essa variavel depois pois ela é descessaria
          long savePosi = arqPrinci.getFilePointer();

          arqPrinci.seek(arqPrinci.getFilePointer() + 2);
          String testeLapide = arqPrinci.readUTF();

          if (testeLapide.equals(" ")) {
            arqPrinci.seek(savePosi);
            iDclube = arqPrinci.readShort();
            lapide = arqPrinci.readUTF();
            ftNome = arqPrinci.readUTF().toUpperCase();
            ftCnpj = arqPrinci.readUTF().toUpperCase();
            ftCidade = arqPrinci.readUTF().toUpperCase();
            receberByteComprimidoNome = realizarCompressao(ftNome);
            receberByteComprimidoCnpj = realizarCompressao(ftCnpj);
            receberByteComprimidoCidade = realizarCompressao(ftCidade);

            byte partidasJogadas = arqPrinci.readByte();
            byte pontos = arqPrinci.readByte();

            b = toByteArray(iDclube, lapide, ftNome, ftCnpj, ftCidade, partidasJogadas, pontos,
                receberByteComprimidoNome, receberByteComprimidoCnpj, receberByteComprimidoCidade);
            arqCompri.writeInt(tamanhodoRegistro);
            arqCompri.write(b);// criar o tobytearray

          } else {
            arqPrinci.seek(savePosi);
            arqPrinci.seek(arqPrinci.getFilePointer() + tamanhodoRegistro);
          }
          contador++;
        }
        // fazer metodo para calcular taxa de compressao
        int taxaCompre = porcentagemDeCompressao(arqCompri.length(), arqPrinci.length());

        if (taxaCompre != -1) {
          System.out.println("-----X-----" + "\n");
          System.out.println("A taxa de compresao total do arquivo foi de " + taxaCompre + "%" + "\n");
          System.out.println("-----X-----" + "\n");
        } else {
          System.out.println("A compressao resultou numa piora no tamanho do arquivo original NESTE CASO !");
        }

      } else {
        System.out
            .println("Error: Arquivo Principal não pode ser compactado pois ele esta vazio, registre algum time !");
      }
      arqIndice.close();
      arqLista.close();
      arqCompri.close();
      arqPrinci.close();
    } catch (Exception e) {
      System.out.println("Erro no compressaoLze: " + e.getMessage());
    }

  }
  // --------------------X-Fim-Métodos-Principais-Compressao-X--------------------//

  // --------------------X-Inicio-Métodos-Principais-Descompressao-X--------------------//

  private String pesquisarDicionario2(int entrada, String[] dici) {

    String letra = "-1";

    for (int j = 0; j < dici.length; j++) {

      if (entrada == j) {

        letra = dici[j];
        j = dici.length + 10;

      } else if (dici[j] == null) {
        letra = "-1";
      }

    }

    return letra;

  }

  // --------------------------------------
  // O método realizarDescompressao realiza a descompressao de acordo com o lzw,
  // recebe como parametro um array de int que é o array de byte do numero grande.
  // --------------------------------------
  private String realizarDescompressao(int[] arqComprimido) {

    String[] dicionario = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
        "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "Ç",
        "Ã", "Á", "Â", "É", "Ê", "Í", "Î", "Ó", "Õ", "Ô", "Ú", "Û", " ", "/", ".", "-" };

    String[] compressaoDicionario = new String[dicionario.length * 3];
    System.arraycopy(dicionario, 0, compressaoDicionario, 0, dicionario.length);

    int tamanhoEntrada = arqComprimido.length;
    int i = 0;
    int contadorDicionario = qtdElementosString(compressaoDicionario);
    int contadorLoop = 1;
    String retornoConcat = "";
    String letraConcat = "";
    boolean travaLimparVetor = false;
    while (i < tamanhoEntrada) {
      boolean trava = false;
      String letraPrimaria = pesquisarDicionario2(arqComprimido[i], compressaoDicionario);
      if (letraPrimaria.contains("?")) {
        String interoRetirada = "";
        for (int j = 0; j < letraPrimaria.length() - 1; j++) {
          char letra = letraPrimaria.charAt(j);
          interoRetirada += letra;
        }
        retornoConcat += interoRetirada;
        retornoConcat += interoRetirada;
        letraConcat = interoRetirada;
      } else {
        retornoConcat += letraPrimaria;
        letraConcat = letraPrimaria;
      }

      int podeAdicionarNoDici = pesquisarNoDicionario(letraConcat, compressaoDicionario);

      if (podeAdicionarNoDici >= 53) {

        char letraMaior = letraConcat.charAt(0);
        String letraMaiorConvert = "";
        letraMaiorConvert += letraMaior;

        String tirarIntero = compressaoDicionario[contadorDicionario];
        String interoRetirada = "";
        for (int j = 0; j < tirarIntero.length() - 1; j++) {
          char letra = tirarIntero.charAt(j);
          interoRetirada += letra;
        }
        String letraConcat2 = interoRetirada + letraMaiorConvert;
        contadorLoop = 1;

        compressaoDicionario[contadorDicionario] = "";
        compressaoDicionario[contadorDicionario] += letraConcat2;
        contadorDicionario++;
        if (i < tamanhoEntrada - 1) {
          compressaoDicionario[contadorDicionario] = "";
          compressaoDicionario[contadorDicionario] += letraConcat + "?";
        }
        letraConcat = "";

        trava = true;

      }

      if (!travaLimparVetor) {
        compressaoDicionario[contadorDicionario] = "";
        travaLimparVetor = true;
      }
      if (contadorLoop == 1 && !trava) {
        if (i < tamanhoEntrada - 1) {
          compressaoDicionario[contadorDicionario] += letraConcat + "?";
        }
      }

      if (contadorLoop == 2) {

        String tirarIntero = compressaoDicionario[contadorDicionario];
        String interoRetirada = "";
        for (int j = 0; j < tirarIntero.length() - 1; j++) {
          char letra = tirarIntero.charAt(j);
          interoRetirada += letra;
        }
        String letraConcat2 = interoRetirada + letraConcat;
        contadorLoop = 1;

        compressaoDicionario[contadorDicionario] = "";
        compressaoDicionario[contadorDicionario] += letraConcat2;
        contadorDicionario++;
        if (i < tamanhoEntrada - 1) {
          compressaoDicionario[contadorDicionario] = "";
          compressaoDicionario[contadorDicionario] += letraConcat + "?";
        }
        letraConcat = "";

      } else {
        letraConcat = "";
      }
      contadorLoop++;
      i++;
    }

    return retornoConcat;

  }

  // --------------------------------------
  // O método toByteArray faz a compressao em um byteArray e retorna esse array de
  // bytes
  // --------------------------------------

  public byte[] toByteArray(int tamanhoRegistro, short idClube, String lapide, String nome, String cnpj, String cidade,
      byte partidasJogadas,
      byte pontos) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    dos.writeInt(tamanhoRegistro);
    dos.writeShort(idClube);
    dos.writeUTF(lapide);
    dos.writeUTF(nome);
    dos.writeUTF(cnpj);
    dos.writeUTF(cidade);
    dos.writeByte(partidasJogadas);
    dos.writeByte(pontos);
    return baos.toByteArray();
  }

  // --------------------------------------
  // O método descompressaoLzw pega registro por registro e vai descomprimindo
  // caso necessário, ele joga o arquivo desejado nos arquivos principais, faz
  // isso com a lista invertida e com o arquivo de indice
  // --------------------------------------

  public void descompressaoLzw(int entrada) {

    // tamanhoArq+id+lapide+boolean(SETIVERBA)+byte-10(correspondenteaofinaldoarray),
    // tamanhoArq+id+lapide+boolean+nome(SenaoTiverBA),

    RandomAccessFile arqPrinci;
    RandomAccessFile arqCompri;
    RandomAccessFile listaPrinci;
    RandomAccessFile indicePrinci;
    ordenacaoexterna oe = new ordenacaoexterna();
    try {

      String caminhodoArqCompri = "src/database/compressão/futebolCompressao" + entrada + ".db";
      String caminhodoArqLista = "src/database/compressão/ListaCompressao" + entrada + ".db";
      String caminhoArqIndice = "src/database/compressão/IndiceCompressao" + entrada + ".db";
      arqPrinci = new RandomAccessFile("src/database/futebol.db", "rw");
      arqCompri = new RandomAccessFile(caminhodoArqCompri, "rw");
      listaPrinci = new RandomAccessFile("src/database/listainvertida.db", "rw");
      indicePrinci = new RandomAccessFile("src/database/aindices.db", "rw");

      if (arqCompri.length() != 0) {

        byte[] pegarLista = Files.readAllBytes(Paths.get(caminhodoArqLista));
        byte[] pegarIndice = Files.readAllBytes(Paths.get(caminhoArqIndice));
        deletaTudo("src/database/aindices.db");
        deletaTudo("src/database/listainvertida.db");
        listaPrinci.write(pegarLista);
        indicePrinci.write(pegarIndice);
        oe.ordenacaoDistribuicao();

        // comecando a descompactar o arquivo futebolprincipal

        short idInicio = arqCompri.readShort();
        arqPrinci.writeShort(idInicio);

        while (arqCompri.getFilePointer() < arqCompri.length()) {

          int tamanhoRegistro = arqCompri.readInt();
          short iD = arqCompri.readShort();
          String lapide = arqCompri.readUTF();
          boolean verificadorByte = arqCompri.readBoolean();
          String nome;

          if (verificadorByte) {
            int n[];
            byte nB = 0;
            int nTeste[] = new int[500];
            inicializarVetor(nTeste);
            int contadornTeste = 0;
            while (nB != -10) {
              nB = arqCompri.readByte();
              if (nB != -10) {
                nTeste[contadornTeste] = nB;
                contadornTeste++;
              }

            }

            int qtdElementosNTeste = qtdElementosInt(nTeste);

            n = new int[qtdElementosNTeste];

            System.arraycopy(nTeste, 0, n, 0, qtdElementosNTeste);
            // mandar o array para o descompactar.
            nome = realizarDescompressao(n);
          } else {
            nome = arqCompri.readUTF();
          }

          verificadorByte = arqCompri.readBoolean();

          String cnpj;
          if (verificadorByte) {

            int cn[];
            byte nB = 0;
            int nTeste[] = new int[500];
            inicializarVetor(nTeste);
            int contadornTeste = 0;
            while (nB != -10) {
              nB = arqCompri.readByte();
              if (nB != -10) {
                nTeste[contadornTeste] = nB;
                contadornTeste++;
              }

            }

            int qtdElementosNTeste = qtdElementosInt(nTeste);

            cn = new int[qtdElementosNTeste];

            System.arraycopy(nTeste, 0, cn, 0, qtdElementosNTeste);
            // mandar o array para o descompactar.

            cnpj = realizarDescompressao(cn);

          } else {

            cnpj = "";

            if (cnpj.equals("")) {
              arqCompri.seek(arqCompri.getFilePointer() + 2);
            }

          }

          verificadorByte = arqCompri.readBoolean();

          String cidade;
          if (verificadorByte) {
            int city[];
            byte nB = 0;
            int nTeste[] = new int[500];
            inicializarVetor(nTeste);
            int contadornTeste = 0;
            while (nB != -10) {
              nB = arqCompri.readByte();
              if (nB != -10) {
                nTeste[contadornTeste] = nB;
                contadornTeste++;
              }

            }

            int qtdElementosNTeste = qtdElementosInt(nTeste);

            city = new int[qtdElementosNTeste];

            System.arraycopy(nTeste, 0, city, 0, qtdElementosNTeste);

            // mandar o array para o descompactar.
            cidade = realizarDescompressao(city);
          } else {

            cidade = arqCompri.readUTF();

          }

          byte pJ = arqCompri.readByte();

          byte pts = arqCompri.readByte();

          // fazer o tobyteArray2;
          byte[] bf;

          bf = toByteArray(tamanhoRegistro, iD, lapide, nome, cnpj, cidade, pJ, pts);
          arqPrinci.write(bf);

        }
        System.out.println("-----X-----" + "\n");
        System.out.println("Registro comprimido foi descomprimido e já se encontra no diretório principal !\n");
        System.out.println("-----X-----" + "\n");
      } else {
        System.out.println("ERRO: alguns dos arquivos se encontra vazio, tente comprimir primeiro !");
      }

      arqPrinci.close();
      arqCompri.close();
      listaPrinci.close();
      indicePrinci.close();

    } catch (Exception e) {
      System.out.println("Erro no descompressaoLzw: " + e.getMessage());
    }

  }

  // --------------------X-FIM-Métodos-Principais-Descompressao-X--------------------//
}
