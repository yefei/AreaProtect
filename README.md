# AreaProtect 领地保护插件

可以让玩家圈地保护自己的领地，功能类似于经典的 Residence 插件，但是功能更简单更容易使用。
与 Residence 插件最大的区别在于 Residence 是圈立方体，而 AreaProtect 圈的是面积，从基岩到最高处全部保护。

## 下载地址
支持版本 Spigot 1.18 服务器
<https://github.com/yefei/AreaProtect/releases>

## 特色
* 可以保护领地内的生物不被玩家所杀害
* 防止活塞推入方块，却不影响领地内的活塞使用
* 圈地时可以形象的看到所圈的区域
* 领地传送可配置为花钱开通
* 领地传送倒计时显示
* 可配置圈地价格计算公式
* 可配置允许圈地的世界
* 可给单独玩家配置领地内权限
* Dynmap 支持

## 未来版本将要加入的功能
* 世界保护 (可以设置世界的权限，如同领地设置)
* 用户分组配置
* 领地区域自动显示 (如同选择显示,方便建造时观测)
* 领地扩容

## 领地权限关系树
权限名称不区分大小写
```
FireSpread | 火焰蔓延
EndermanPickup | 末影人拿取方块
Piston | 活塞推入

Explosion | 爆炸破坏
 ├─ BedExplosion | 床爆炸
 ├─ Creeper | 爬行者爆炸
 ├─ Fireball | 火焰弹爆炸
 ├─ TNT | TNT爆炸
 └─ WitherExplosion | 凋零爆炸

Flow | 液体流动
 ├─ LavaFlow | 岩浆流动
 └─ WaterFlow | 水流动

All | 全部权限
 ├─ Damage | 伤害
 │  └─ PVP | PVP
 ├─ Build | 建造
 │  ├─ Place | 放置
 │  │  └─ Ignite | 点火
 │  ├─ Destroy | 破坏
 │  │  └─ Trample | 踩踏庄稼
 │  ├─ Bucket | 使用桶
 │  │  ├─ LavaBucket | 使用岩浆桶
 │  │  └─ WaterBucket | 使用水桶
 │  └─ DragonEgg | 碰触龙蛋
 ├─ Use | 使用 (所有可交互物品)
 └─ TP | 传送
```

# 玩家教程
## 圈地
- 手持木棍左键点击方块选择起点
- 右键点击方块选择结束点 (选择区域的对角线)
- 选择完成即可看到选择的区域
- 输入 /area create <名称> 创建领地

## 领地指令
所有 [] 内的参数是选填项
所有 <> 内的参数为必填项
如果你站在领地内则不需要输入 [领地] 参数
```
/area admin                                     激活/关闭管理员模式
/area select                                    查看已选区域
/area select <X直径> <Z径直>                    以你为中心输入直径选择区域
/area create <领地名称>                         创建领地
/area my                                        列出自己的领地
/area info [领地]                               查看领地信
/area flag set <权限> yes/no [领地]             设置领地权限
/area flag remove <权限> [领地]                 删除一个权限
/area flag reset [领地]                         重置领地权限设置为默认值
/area flag clear [领地]                         清除领地的所有权限设置
/area flag copy <目标领地> [领地]               复制目标领地的所有权限设置
/area player set <玩家> <权限> yes/no [领地]    设置玩家在领地的权限
/area player remove <玩家> <权限> [领地]        删除一个玩家权限
/area player clear <玩家> [领地]                清除玩家所有权限设置
/area delete <领地名称>                         删除领地
/area list                                      列出所有领地
/area tp <领地>                                 传送到目标领地
/area tpset                                     设置当前位置为传送点
/area setowner <新主人> [领地]                  设置领地主人
/area serverland [领地]                         将领地设置为服务器土地
```