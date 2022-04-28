package chess.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import chess.dao.ChessGameDao;
import chess.dao.PieceDao;
import chess.domain.Command;
import chess.dto.ChessGameDto;

@SpringBootTest
@Transactional
class ChessServiceTest {
    private ChessService chessService;
    private int savedId;

    @Autowired
    private ChessGameDao chessGameDao;
    @Autowired
    private PieceDao pieceDao;

    @BeforeEach
    void setUp() {
        chessService = new ChessService(chessGameDao, pieceDao);
        savedId = chessService.save("test_game1", "1234");
        chessService.save("test_game2", "1234");
        chessService.save("test_game3", "1234");
    }

    @Test
    @DisplayName("게임을 한개 더 생성하면 4개가 되어야 합니다.")
    void testGamesSize() {
        chessService.save("test_game4", "1234");
        List<ChessGameDto> games = chessService.findAllChessGames();
        assertThat(games).hasSize(4);
    }

    @Test
    @DisplayName("게임을 생성하면 말의 개수가 32개여야 합니다.")
    void testPiecesOfGames() {
        int savedId = chessService.save("test_game4", "1234");
        List<String> chessBoard = chessService.findChessBoardById(savedId);
        List<String> existPieces = chessBoard.stream()
            .filter(piece -> !piece.equals(""))
            .collect(Collectors.toList());

        assertThat(existPieces).hasSize(32);
    }

    @Test
    @DisplayName("진행중인 게임을 삭제하면 예외를 반환해야 합니다.")
    void deleteNotEndGame() {
        assertThatThrownBy(() -> chessService.delete("1234", savedId))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("게임을 삭제할 때 비밀번호가 틀리면 삭제하지 않아야 합니다.")
    void deleteWrongPassword() {
        chessService.finish(Command.from("end"), savedId);
        chessService.delete("123", savedId);
        List<String> chessBoard = chessService.findChessBoardById(savedId);
        assertThat(chessBoard).isNotNull();
    }

    @Test
    @DisplayName("끝난 게임을 정확한 비밀번호로 삭제할 시 오류가 발생하지 않아야 합니다.")
    void delete() {
        chessService.finish(Command.from("end"), savedId);
        assertDoesNotThrow(() -> chessService.delete("1234", savedId));
    }

    @Test
    @DisplayName("게임을 종료하면 게임의 상태는 종료상태여야 합니다.")
    void end() {
        chessService.finish(Command.from("end"), savedId);
        assertThat(chessService.isEnd(savedId)).isTrue();
    }
}