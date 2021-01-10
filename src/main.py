#  MIT License
#
#  Copyright (c) 2021 Over-Run
#
#  Permission is hereby granted, free of charge, to any person obtaining a copy
#  of this software and associated documentation files (the "Software"), to deal
#  in the Software without restriction, including without limitation the rights
#  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
#  copies of the Software, and to permit persons to whom the Software is
#  furnished to do so, subject to the following conditions:
#
#  The above copyright notice and this permission notice shall be included in all
#  copies or substantial portions of the Software.
#
#  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
#  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
#  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
#  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
#  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
#  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
#  SOFTWARE.

import configparser
import os


def config_get_or_default(config_parser: configparser.ConfigParser,
                          section: str,
                          option: str,
                          default=''):
    try:
        return config_parser.get(section=section, option=option)
    except configparser.Error:
        return default


def equals_one_ignore_case(a: str, b: [str]) -> bool:
    for s in b:
        if a.upper() == s.upper():
            return True
    return False


if __name__ == '__main__':
    try:
        file = open('config.ini')
    except FileNotFoundError:
        file = open('config.ini', 'w', encoding='utf-8')
        file.write('[config]\n')
        file.write('target=\n')
        file.write('lang=en_us\n\n')
        file.write('[lang_en_us]\n')
        file.write('should_start=Do you want to start game?\n')
        file.write('should_restart=Do you want to restart program?\n\n')
        file.write('[lang_zh_cn]\n')
        file.write('should_start=你想要启动游戏吗？\n')
        file.write('should_restart=你想要重启程序吗？\n\n')
        file.write('[lang_zh_tw]\n')
        file.write('should_start=你想要啟動遊戲嗎？\n')
        file.write('should_restart=你想要重啟程序嗎？')
    file.close()
    config = configparser.ConfigParser()
    config.read('config.ini', encoding='utf-8')
    lang = config_get_or_default(config, 'config', 'lang', 'en_us')
    restart = True
    while restart:
        should_start = str(input(config_get_or_default(config, 'lang_' + lang, 'should_start',
                                                       'Do you want to start game?') + ' (Y/N)\n'))
        if equals_one_ignore_case(should_start, ['y', 'yes', '1']):
            os.system('cmd /c java -jar ' + config.get('config', 'target'))
        restart = equals_one_ignore_case(str(input(config_get_or_default(config,
                                                                         'lang_' + lang,
                                                                         'should_restart',
                                                                         'Do you want to restart program?') + ' (Y/N)\n')),
                                         ['y', 'yes', '1'])
