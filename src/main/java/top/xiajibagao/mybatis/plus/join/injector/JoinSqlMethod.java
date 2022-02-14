package top.xiajibagao.mybatis.plus.join.injector;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 自定义sql方法
 *
 * @author huangchengxing
 * @date 2022/02/10 13:55
 */
@Getter
@RequiredArgsConstructor
public enum JoinSqlMethod {


    SELECT_MAPS("selectMapsJoin", "查询满足条件所有数据", "<script>\n%s \nSELECT %s \nFROM %s %s %s %s\n</script>"),
    SELECT_MAPS_PAGE("selectMapsPageJoin", "查询满足条件所有数据（并翻页）", "<script>\n%s \nSELECT %s \nFROM %s %s %s %s\n</script>"),
    SELECT_OBJS("selectObjsJoin", "查询满足条件所有数据", "<script>\n%s \nSELECT %s \nFROM %s %s %s %s\n</script>"),
    SELECT_LIST_JOIN("selectListJoin", "查询数据", "<script>\n%s \nSELECT %s \nFROM %s %s %s %s\n</script>"),
    SELECT_PAGE_JOIN("selectPageJoin", "查询数据（并翻页）", "<script>\n%s \nSELECT %s \nFROM %s %s %s %s\n</script>"),
    EXISTS_JOIN("selectExistsJoin", "查询数据是否存在", "<script>\n%s \nSELECT EXISTS (SELECT 1 FROM %s %s %s)%s\n</script>"),
    SELECT_COUNT_JOIN("selectCountJoin", "查询数据", "<script>\n%s \nSELECT count(1) \nFROM %s %s %s %s\n</script>");

    private final String method;
    private final String desc;
    private final String sql;

}
