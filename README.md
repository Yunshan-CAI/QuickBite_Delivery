# ErrorCollection 异常集锦

在复刻黑马的“瑞吉外卖”的springboot项目过程中，尤其是在项目前期遇到了一些把我卡住了一两天的error。问朋友、查技术文档最终解决了这些问题，解决异常的过程都实实在在地提高了我解决问题的能力。And that makes programming fun.

但是我自己没有发现这个项目有探讨问题的社区，虽然弹幕上有时候有一些有帮助的solution，但是总归不够系统，且弹幕信息庞杂，有用的信息密度低，因此觉得有必要把我在创建项目过程中碰到的觉得会比较普遍的问题总结一下，希望会帮到一些人:)

# Errors (按项目进度排序)


## day01 那个我一直没有找到问题的错误，再把文件夹下载一下看看问题到底出在哪里

## day02 加了WebMvcConfig类之后

## day04 

在实现上传功能时，在yml里面path的书写格式要注意，应该是这样的：

```
项目名:
  path: 存放的文件夹路径
```
注意项目名一定要顶格写，path冒号后面要留空格，文件夹路径后面要加/，如 /Users/wentibaobao/Desktop/QuickBite Delivery/upload/

否则会容易出现BeanCreationException和IllegalArgumentException。

