package examples.gp.tictactoe;

public class GameWonException
    extends RuntimeException {

  private int m_color;

  public GameWonException(int a_color, String a_message) {
    super(a_message);
    m_color = a_color;
  }

  public int getColor() {
    return m_color;
  }
}
