## リモートマシンのコマンドをssh経由で実行します
> java -jar JAR_PATH リモートホスト名 コマンド

### 前提条件
実行するユーザの`~/.ssh/config`にリモート接続先の設定がされているかつ、sshキーのパスフレーズなしでリモートマシンにssh接続できるものとします。
`~/.ssh/config`のファイルフォーマットは以下になります。
```
Host ホスト1
    HostName        ホスト
    Port            ポート
    IdentityFile    sshKeyファイル
    User            接続ユーザ

Host ホスト2
    HostName        ホスト
    Port            ポート
    IdentityFile    sshKeyファイル
    User            接続ユーザ
```

ホスト1のリモート端末でdateコマンドを実行する場合は以下のようになります。
> java -jar JAR_PATH ホスト1 date