package chess.dto;

import chess.domain.ChessBoard;
import chess.domain.ChessGame;
import chess.domain.state.State;

public class ChessGameDto {
    private int chessGameId;
    private ChessBoardDto chessBoard;
    private String gameName;
    private String turn;

    public ChessGameDto(int chessGameId, String gameName) {
        this.chessGameId = chessGameId;
        this.gameName = gameName;
    }

    public ChessGameDto(int chessGameId, String gameName, String turn) {
        this.chessGameId = chessGameId;
        this.gameName = gameName;
        this.turn = turn;
    }

    private ChessGameDto(ChessBoardDto chessBoardDto, String gameName, String turn) {
        this.chessBoard = chessBoardDto;
        this.gameName = gameName;
        this.turn = turn;
    }

    public static ChessGameDto from(ChessGame chessGame) {
        ChessBoard chessBoard = chessGame.getChessBoard();
        ChessBoardDto chessBoardDto = ChessBoardDto.from(chessBoard.getCells());

        String gameName = chessGame.getGameName();

        State state = chessGame.getState();
        String turn = state.getTurn();

        return new ChessGameDto(chessBoardDto, gameName, turn);
    }

    public int getChessGameId() {
        return this.chessGameId;
    }

    public String getGameName() {
        return this.gameName;
    }

    public String getTurn() {
        return turn;
    }

    public ChessBoardDto getChessBoard() {
        return chessBoard;
    }
}
